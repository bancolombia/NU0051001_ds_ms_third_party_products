package co.com.bancolombia.rest.config;

import co.com.bancolombia.d2b.model.oauth.ClientCredentialsFlowClient;
import co.com.bancolombia.d2b.webclient.D2BWebClientFactory;
import co.com.bancolombia.d2b.webclient.cognitoauth.CognitoAuthFilterFunction;
import co.com.bancolombia.d2b.webclient.model.WebClientRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {


    @Bean(name = "d2bWebClient")
    public WebClient d2bWebClient(D2BWebClientFactory d2bWebClientFactory,
                                  @Qualifier("credentialsExchangeFunction")
                                  ExchangeFilterFunction cognitoCredentialsExchangeFunction,
                                  @Value("${adapter.rest-consumer.channel-management.base-url}")
                                  String baseUrl,
                                  @Value("${adapter.rest-consumer.channel-management.timeout}")
                                  Integer timeout) {
        return d2bWebClientFactory.createWebClientFor(
                WebClientRequest.builder()
                        .exchangeFilterFunction(cognitoCredentialsExchangeFunction)
                        .urlBase(baseUrl)
                        .connTimeout(timeout)
                        .readTimeout(timeout)
                        .writeTimeout(timeout)
                        .build()
        );
    }

    @Bean(name = "apicWebClient")
    public WebClient apicWebClient(D2BWebClientFactory d2bWebClientFactory,
                                   ExchangeFilterFunction apicCredsFilterFunction,
                                   @Value("${adapter.rest-consumer.deposits.timeout}")
                                   Integer timeout) {
        return d2bWebClientFactory.createWebClientFor(
                WebClientRequest.builder()
                        .exchangeFilterFunction(apicCredsFilterFunction)
                        .connTimeout(timeout)
                        .readTimeout(timeout)
                        .writeTimeout(timeout)
                        .build()
        );
    }

    @Bean(name = "cognitoWebClient")
    public WebClient cognitoWebClient(D2BWebClientFactory d2bWebClientFactory,
                                      @Qualifier("cognitoAuthFilterFunction") ExchangeFilterFunction
                                              cognitoCredentialsExchange) {
        return d2bWebClientFactory.createWebClientFor(
                WebClientRequest.builder()
                        .exchangeFilterFunction(cognitoCredentialsExchange)
                        .build()
        );
    }

    @Bean(name = "tokenCredentialsWebClient")
    public WebClient tokenCredentialsWebClient(D2BWebClientFactory d2bWebClientFactory,
                                               ExchangeFilterFunction apicCredsFilterFunction,
                                               @Qualifier("apicCredentialsTokenFilterFunction")
                                               ExchangeFilterFunction exchangeFilterFunctionToken,
                                               @Value("${adapter.rest-consumer.payments.timeout}")
                                               Integer timeout) {
        return d2bWebClientFactory.createWebClientFor(
                WebClientRequest.builder()
                        .exchangeFilterFunction(apicCredsFilterFunction)
                        .exchangeFilterFunction(exchangeFilterFunctionToken)
                        .connTimeout(timeout)
                        .readTimeout(timeout)
                        .writeTimeout(timeout)
                        .build()
        );
    }

    @Bean
    public ExchangeFilterFunction credentialsExchangeFunction(
            ClientCredentialsFlowClient credentialsFlowClient) {
        return new CognitoAuthFilterFunction(credentialsFlowClient);
    }


}