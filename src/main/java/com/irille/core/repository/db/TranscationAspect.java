package com.irille.core.repository.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TranscationAspect {
	
	@Pointcut("execution(@com.irille.core.repository.db.Transcation * *.*(..))")
	public void transcaton() {
		
	}
	
	@Before("transcaton()")
	public void closeAutoCommit(JoinPoint joinPoint) {
		p(joinPoint.getTarget());
		p(joinPoint.getThis());
		p(joinPoint.getArgs());
		p(joinPoint.getKind());
		p(joinPoint.getSignature());
		p(joinPoint.getSourceLocation());
		p(joinPoint.getStaticPart());
		System.out.println("closeAutocCommit");
	}
	
	@After("transcaton()")
	public void connectionCommit() {
		System.out.println("connectionCommit");
	}
	public void p(Object o) {
		System.out.println(o);
	}
}
