package com.ctrip.framework.apollo.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.actuate.system.EmbeddedServerPortFileWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ctrip.framework.apollo.biz.ApolloBizConfig;
import com.ctrip.framework.apollo.biz.service.AppNamespaceService;
import com.ctrip.framework.apollo.biz.service.AppService;
import com.ctrip.framework.apollo.biz.service.ClusterService;
import com.ctrip.framework.apollo.common.ApolloCommonConfig;
import com.ctrip.framework.apollo.common.entity.App;
import com.ctrip.framework.apollo.common.entity.AppNamespace;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;

@EnableAspectJAutoProxy
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = { ApolloCommonConfig.class, ApolloBizConfig.class })
public class DBInitApplication implements CommandLineRunner {
	@Autowired
	AppService appService;
	@Autowired
	AppNamespaceService appNSS;
	@Autowired
	ClusterService clusters;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(DBInitApplication.class).run(args);
		context.addApplicationListener(new ApplicationPidFileWriter());
		context.addApplicationListener(new EmbeddedServerPortFileWriter());
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			App app = new App();
			app.setAppId("SampleApp");
			app.setName("Sample App");
			app.setOrgId("TEST1");
			app.setOrgName("样例部门1");
			app.setOwnerName("apollo");
			app.setOwnerEmail("apollo@acme.com");
			appService.save(app);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AppNamespace appNamespace = new AppNamespace();
			appNamespace.setName("application");
			appNamespace.setAppId("SampleApp");
			appNamespace.setFormat(ConfigFileFormat.Properties.getValue());
			appNamespace.setPublic(false);
			appNamespace.setComment("default app namespace");
			appNSS.createAppNamespace(appNamespace);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			clusters.createDefaultCluster("SampleApp", "default");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
