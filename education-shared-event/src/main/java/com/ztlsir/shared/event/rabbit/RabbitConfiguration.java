package com.ztlsir.shared.event.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztlsir.shared.event.DomainEventSender;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableConfigurationProperties(EducationRabbitProperties.class)
public class RabbitConfiguration {
    private EducationRabbitProperties educationRabbitProperties;
    private org.springframework.boot.autoconfigure.amqp.RabbitProperties rabbitProperties;

    public RabbitConfiguration(EducationRabbitProperties educationRabbitProperties,
                               RabbitProperties rabbitProperties) {
        this.educationRabbitProperties = educationRabbitProperties;
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(rabbitProperties.getHost());
        factory.setAddresses(rabbitProperties.getAddresses());
        factory.setUsername(rabbitProperties.getUsername());
        factory.setPassword(rabbitProperties.getPassword());
        factory.setPort(rabbitProperties.getPort());
        factory.setVirtualHost(rabbitProperties.getVirtualHost());
        return factory;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        messageConverter.setClassMapper(classMapper());
        return messageConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");
        return classMapper;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setPrefetchCount(rabbitProperties.getListener().getSimple().getPrefetch());
        factory.setConcurrentConsumers(rabbitProperties.getListener().getSimple().getConcurrency());
        factory.setMaxConcurrentConsumers(rabbitProperties.getListener().getSimple().getMaxConcurrency());
        factory.setMessageConverter(messageConverter);
        factory.setTaskExecutor(taskExecutor());

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(1000L);
        RetryOperationsInterceptor retryOperationsInterceptor = RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffPolicy(fixedBackOffPolicy)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
        factory.setAdviceChain(retryOperationsInterceptor);

        return factory;
    }


    private TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setThreadNamePrefix("rabbitmq-listener-executor-");
        executor.initialize();
        return executor;
    }

    //"发送方Exchange"
    @Bean
    public TopicExchange publishExchange() {
        return new TopicExchange(educationRabbitProperties.getPublishX(),
                true,
                false);
    }

    @Bean
    public DomainEventSender domainEventSender(MessageConverter messageConverter,
                                               EducationRabbitProperties properties,
                                               RabbitTemplate rabbitTemplate) {
        return new RabbitDomainEventSender(messageConverter, properties, rabbitTemplate);
    }

}
