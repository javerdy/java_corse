package ru.pocket.testcat.addressbook.tests;

import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.pocket.testcat.addressbook.appmanager.ApplicationManadger;
import ru.pocket.testcat.addressbook.model.ContactData;
import ru.pocket.testcat.addressbook.model.Contacts;
import ru.pocket.testcat.addressbook.model.GroupData;
import ru.pocket.testcat.addressbook.model.Groups;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Goblik on 26.08.2016.
 */
public class TestBase {

  Logger logger = LoggerFactory.getLogger(TestBase.class);


  protected static final ApplicationManadger app
          = new ApplicationManadger(System.getProperty("browser", BrowserType.FIREFOX));

  @BeforeSuite
  public void setUp() throws Exception {
    app.init();
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() {
    app.stop();
  }


  @BeforeMethod
  public void logTestStart(Method m, Object[] p){
    logger.info("Start test" +m.getName() +"with parameters" + Arrays.asList(p));

  }

  @AfterMethod(alwaysRun = true)
  public  void logTestStop(Method m){
    logger.info("Stop test " + m.getName());

  }
  public void verifyGroupListInUI() {
    if (Boolean.getBoolean("verifyUI")) {
      Groups dbGroups = app.db().groups();
      Groups uaGroups = app.group().all();
      assertThat(uaGroups, equalTo(dbGroups.stream()
              .map((g) -> new GroupData().withGroupid(g.getGroupid()).withGroupname(g.getGroupname()))
              .collect(Collectors.toSet())));
    }
  }

  public void verifyContactListInUI() {
    if (Boolean.getBoolean("verifyUI")) { //-DverifyUI=true
      Contacts dbContacts = app.db().contacts();
      Contacts uiContacts = app.contact().all();
      assertThat(uiContacts, equalTo(dbContacts.stream()
              .map((g) -> new ContactData().withId(g.getId()).withFirstname(g.getFirstname())
                      .withLastname(g.getLastname())).collect(Collectors.toSet())));
    }
  }
}
