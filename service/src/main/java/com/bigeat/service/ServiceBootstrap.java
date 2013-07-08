package com.bigeat.service;

import com.bigeat.service.configuration.BigEatConfiguration;
import com.bigeat.service.core.BigEatService;
import com.bigeat.service.dal.BigEatImageRepository;
import com.bigeat.service.dal.BigEatRepository;
import com.bigeat.service.dal.mem.InMemoryBigEatImageRepository;
import com.bigeat.service.dal.mem.InMemoryBigEatRepository;
import com.bigeat.service.health.BigEatHealthCheck;
import com.bigeat.service.resource.RootResource;
import com.sun.jersey.core.impl.provider.entity.ByteArrayProvider;
import com.sun.jersey.core.impl.provider.entity.InputStreamProvider;
import com.sun.jersey.core.impl.provider.entity.StringProvider;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * @author mattt
 * 
 */
public class ServiceBootstrap extends Service<BigEatConfiguration> {
  public static void main(final String[] args) throws Exception {
    new ServiceBootstrap().run(args);
  }

  @Override
  public void initialize(final Bootstrap<BigEatConfiguration> bootstrap) {
    bootstrap.setName("bigeats-service");

    // bundles
    bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
  }

  @Override
  public void run(final BigEatConfiguration configuration, final Environment environment) {

    // cheese that you have to do this manually...
    environment.addProvider(StringProvider.class);
    environment.addProvider(InputStreamProvider.class);
    environment.addProvider(ByteArrayProvider.class);

    final BigEatRepository bigEatRepository = new InMemoryBigEatRepository();
    final BigEatImageRepository imageRepository = new InMemoryBigEatImageRepository();

    final BigEatService service = new BigEatService(bigEatRepository, imageRepository);

    // resources
    environment.addResource(new RootResource(service));

    // health checks
    environment.addHealthCheck(new BigEatHealthCheck());
  }

}
