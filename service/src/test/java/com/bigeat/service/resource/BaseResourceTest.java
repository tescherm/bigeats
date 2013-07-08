package com.bigeat.service.resource;

import static com.bigeat.service.test.BigEatTestUtil.testService;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.yammer.dropwizard.testing.ResourceTest;

/**
 * @author mattt
 */
public abstract class BaseResourceTest extends ResourceTest {

  /*
   * Wraps {@link ResourceTest}'s {@link Before} and {@link After} JUnit annotations
   */

  @BeforeClass
  public void testngSetUpJersey() throws Exception {
    this.setUpJersey();
  }

  @AfterClass
  public void testngTearDownJersey() throws Exception {
    this.tearDownJersey();
  }

  @Override
  protected void setUpResources() {
    addResource(new RootResource(testService()));
  }

}
