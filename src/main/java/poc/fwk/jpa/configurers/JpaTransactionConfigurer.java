package poc.fwk.jpa.configurers;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lombok.Setter;

@Configuration
@Aspect
@Order(Integer.MAX_VALUE)
@ConditionalOnSingleCandidate(JpaTransactionConfigurer.class)
public class JpaTransactionConfigurer implements ApplicationContextAware {

	@Setter
	private ApplicationContext applicationContext;

	@Pointcut("@within(org.springframework.stereotype.Service)")
	public void service() {
		// Method is empty as this is just a Pointcut, the implementations are in the advices.
	}

	@Pointcut("@within(org.springframework.transaction.annotation.Transactional)"
			+ " || @annotation(org.springframework.transaction.annotation.Transactional)"
			+ " || @within(javax.transaction.Transactional)"
			+ " || @annotation(javax.transaction.Transactional)")
	public void transactional() {
		// Method is empty as this is just a Pointcut, the implementations are in the advices.
	}

	@Around("service() && !transactional()")
	public Object interceptTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
		PlatformTransactionManager txManager = applicationContext.getBean(PlatformTransactionManager.class);
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		transactionDefinition.setReadOnly(true);
		txManager.getTransaction(transactionDefinition);
		return joinPoint.proceed();
	}

}
