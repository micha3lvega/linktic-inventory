package co.com.linktic.services.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Configuración de OpenAPI para integrar la autenticación por API Key en Swagger UI. Define un esquema de seguridad para 'x-api-key' en el header y lo aplica globalmente.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Mi API", version = "1.0", description = "Documentación de la API con autenticación por API Key"), security = @SecurityRequirement(name = "apiKey"))
@SecurityScheme(name = "apiKey", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "x-api-key", description = "Requiere una API Key válida en el encabezado 'x-api-key' para acceder a los servicios.")
public class SwaggerSecurityConfig {

}
