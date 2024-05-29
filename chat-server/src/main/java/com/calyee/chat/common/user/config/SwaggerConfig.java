package com.calyee.chat.common.user.config;

/**
 * Description: swagger配置
 */
//@Configuration
//@ConditionalOnProperty(name = "swagger.enable", havingValue = "true", matchIfMissing = true)
//@EnableSwagger2WebMvc
//public class SwaggerConfig {
//    @Bean(value = "defaultApi2")
//    Docket docket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                //配置网站的基本信息
//                .apiInfo(new ApiInfoBuilder()
//                        //网站标题
//                        .title("CALYEECHAT接口文档")
//                        //标题后面的版本号
//                        .version("v1.0")
//                        .description("CALYEECHAT接口文档")
//                        //联系人信息
//                        .contact(new Contact("Calyee", "<http://blog.calyee.top>", "599882460@qq.com"))
//                        .build())
//                .select()
//                //指定接口的位置
//                .apis(RequestHandlerSelectors
//                        .withClassAnnotation(RestController.class)
//                )
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    @Bean
//    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
//        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
//        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//        allEndpoints.addAll(webEndpoints);
//        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//        String basePath = webEndpointProperties.getBasePath();
//        EndpointMapping endpointMapping = new EndpointMapping(basePath);
//        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
//        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
//    }
//
//    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
//        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
//    }
//}