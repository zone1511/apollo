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
import org.yaml.snakeyaml.Yaml;

import com.ctrip.framework.apollo.biz.ApolloBizConfig;
import com.ctrip.framework.apollo.common.ApolloCommonConfig;
import com.ctrip.framework.apollo.common.entity.App;
import com.ctrip.framework.apollo.common.entity.AppNamespace;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.openapi.PortalOpenApiConfig;
import com.ctrip.framework.apollo.openapi.service.ConsumerService;
import com.ctrip.framework.apollo.portal.PortalApplication;
import com.ctrip.framework.apollo.portal.entity.po.Permission;
import com.ctrip.framework.apollo.portal.entity.po.ServerConfig;
import com.ctrip.framework.apollo.portal.entity.po.UserPO;
import com.ctrip.framework.apollo.portal.repository.ServerConfigRepository;
import com.ctrip.framework.apollo.portal.repository.UserRepository;
import com.ctrip.framework.apollo.portal.repository.UserRoleRepository;
import com.ctrip.framework.apollo.portal.service.AppNamespaceService;
import com.ctrip.framework.apollo.portal.service.AppService;
import com.ctrip.framework.apollo.portal.service.RolePermissionService;

@EnableAspectJAutoProxy
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = { ApolloCommonConfig.class, PortalApplication.class, PortalOpenApiConfig.class })
public class PortalDBInitApplication implements CommandLineRunner {
	@Autowired
	AppService appService;
	@Autowired
	AppNamespaceService appNSS;
	@Autowired
	ConsumerService conService;
	@Autowired
	ServerConfigRepository serverConfigRepository;

	@Autowired
	UserRoleRepository userRoleRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	RolePermissionService roleService;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(PortalDBInitApplication.class).run(args);
		context.addApplicationListener(new ApplicationPidFileWriter());
		context.addApplicationListener(new EmbeddedServerPortFileWriter());
	}

	@Override
	public void run(String... args) throws Exception {
		Yaml yaml = new Yaml();

		try {
			App app = new App();
			app.setAppId("SampleApp");
			app.setName("Sample App");
			app.setOrgId("TEST1");
			app.setOrgName("样例部门1");
			app.setOwnerName("apollo");
			app.setOwnerEmail("apollo@acme.com");
			// appService.save(app);
			System.out.println(yaml.dump(app));
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
			// appNSS.createAppNamespace(appNamespace);
			System.out.println(yaml.dump(appNamespace));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ServerConfig sConfig = new ServerConfig();
			sConfig.setKey("key");
			sConfig.setValue("value");
			sConfig.setComment("comment");
			serverConfigRepository.save(sConfig);
			System.out.println(yaml.dump(sConfig));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			UserPO user = new UserPO();
			user.setUsername("apollo");
			user.setPassword("$2a$10$7r20uS.BQ9uBpf3Baj3uQOZvMVvB1RN3PYoKE94gtz2.WAOuiiwXS");
			user.setEmail("asd@asd.com");
			user.setEnabled(1);
			userRepo.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Permission permission = new Permission();
		// permission.setId(1);
		// permission.setPermissionType("CreateCluster");
		// permission.setTargetId("SampleApp");
		//
		// roleService.createPermission(permission);
		//
		// roleService.createRoleWithPermissions(role, permissionIds)
	}

}
