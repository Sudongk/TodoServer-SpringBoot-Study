package com.example.springboot.common.jwt;

import com.example.springboot.common.exception.InValidJwtException;
import com.example.springboot.member.domain.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;


@Component
public class JwtProvider {

    private final SecretKeySpec key;

    // 기본적으로 스프링 IoC 컨테이너는 빈들을 등록한 순서대로 빈들을 생성하고 의존성 주입을 수행합니다.
    // 따라서 TokenRequiredAspect 빈이 먼저 생성되고, 이 때 JwtProvider의 secretKey 값을 주입하려고 하지만,
    // 이 값은 JwtProvider 빈이 생성되어야 생성될 수 있기 때문에 null이 됩니다.
    // 그리고 이후 JwtProvider 빈이 생성되는데, 이미 null로 초기화된 secretKey를 사용하게 되므로 NullPointerException 오류가 발생한다.
    // 이 문제를 해결하려면 JwtProvider를 먼저 생성하고, 그 다음에 TokenRequiredAspect 빈이 생성되도록 하는 방법이 있지만,
    // 이는 복잡하고 가독성이 떨어지는 방법입니다. 더 좋은 해결책은 JwtProvider의 secretKey 값을 외부에서 주입받는 것
    public JwtProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(final Member member) {
        return Jwts.builder()
                // payload
                .claim("memberId", member.getId())
                .claim("name", member.getName())
                .claim("age", member.getAge())
                // 유효 기간
                .setExpiration(new Date(System.currentTimeMillis() + 120000))
                // signature
                .signWith(key)
                .compact();
    }

    public Claims getClaims(final String token) {
        return (Claims) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parse(token)
                .getBody();
    }

    public String extractJwt(ServletRequestAttributes requestAttributes) {
        return Optional.ofNullable(requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION))
                .filter(auth -> auth.startsWith("Bearer "))
                .map(auth -> auth.replace("Bearer ", ""))
                .orElseThrow(InValidJwtException::new);
    }

    public String extractJwt (NativeWebRequest webRequest) {
        return Optional.ofNullable(webRequest.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(auth -> auth.startsWith("Bearer "))
                .map(auth -> auth.replace("Bearer ", ""))
                .orElseThrow(InValidJwtException::new);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaims(token);

        return claimsResolver.apply(claims);
    }

    public Long extractMemberId(final String token) {
        return extractClaim(token, claims -> claims.get("memberId", Long.class));
    }

    public String extractMemberName(final String token) {
        return extractClaim(token, claims -> claims.get("name", String.class));
    }

    public Integer extractMemberAge(final String token) {
        return extractClaim(token, claims -> claims.get("age", Integer.class));
    }
}
