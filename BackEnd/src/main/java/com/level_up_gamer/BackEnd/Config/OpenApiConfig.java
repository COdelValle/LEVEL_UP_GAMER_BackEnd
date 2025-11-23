package com.level_up_gamer.BackEnd.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger) para documentación automática de la API
 * 
 * Proporciona:
 * - Documentación detallada de todos los endpoints
 * - Información de seguridad (JWT y API Key)
 * - Ejemplos de requests y responses
 * - Interfaz Swagger UI accesible en /swagger-ui.html
 * 
 * @author LEVEL UP GAMER Development Team
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Define la configuración principal de OpenAPI con información
     * sobre la API, servidores, y esquemas de seguridad
     * 
     * @return OpenAPI configurado con todos los detalles de la API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Información básica de la API
                .info(new Info()
                        .title("LEVEL UP GAMER API")
                        .version("1.0.0")
                        .description("API Backend para la plataforma de gaming LEVEL UP GAMER. " +
                                "Incluye autenticación segura con JWT, gestión de usuarios, " +
                                "catálogo de productos, procesamiento de órdenes y blog de contenido.")
                        .contact(new Contact()
                                .name("LEVEL UP GAMER Development Team")
                                .email("dev@levelupgamer.com")
                                .url("https://www.levelupgamer.com")
                        )
                )
                
                // Servidores de desarrollo y producción
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor de desarrollo")
                )
                .addServersItem(new Server()
                        .url("https://api.levelupgamer.com")
                        .description("Servidor de producción")
                )
                
                // Esquemas de seguridad
                .components(new Components()
                        // Esquema JWT Bearer Token
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtenido en /api/auth/login. " +
                                                "Ejemplo: Bearer eyJhbGciOiJIUzUxMiJ9...")
                        )
                        // Esquema API Key
                        .addSecuritySchemes("apiKeyAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-API-Key")
                                        .description("API Key obtenida en /api/auth/register. " +
                                                "Se envía en el header X-API-Key para autenticación de máquina a máquina")
                        )
                )
                
                // Requerimiento de seguridad por defecto (aplica a todos los endpoints protegidos)
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth")
                        .addList("apiKeyAuth")
                );
    }
}
