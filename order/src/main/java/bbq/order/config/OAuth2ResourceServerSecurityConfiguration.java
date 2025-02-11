package bbq.order.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

/**
 * OAuth resource configuration.
 */
@Configuration
@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration {

    // @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    //String jwkSetUri;
    @Value("${spring.security.oauth2.resourceserver.issuer-uri}")
    String issuer;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/menu/**").permitAll()
                        .requestMatchers("/api/order-to-kitchen/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer((oauth2) -> oauth2
                                .jwt(Customizer.withDefaults())
//                .oauth2ResourceServer(oauth2 -> oauth2
                        //                                    .jwt(jwt -> jwt
                        //  .jwkSetUri("http://localhost:8080/realms/freddy/protocol/openid-connect/certs")
                        //)
                );
//                .oauth2ResourceServer((oauth2) -> oauth2
        //              .jwt(Customizer.withDefaults())
        //);
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuer);
    }
/**
 @Bean JwtDecoder jwtDecoder() {
 return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
 }
 */
}
