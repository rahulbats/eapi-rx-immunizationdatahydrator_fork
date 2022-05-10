import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(
        classes = {

        }, loader = AnnotationConfigContextLoader.class, initializers = ConfigDataApplicationContextInitializer.class)
public class EapirximmunizationdatahydratorUnitTests {

    private static final Logger logger = LoggerFactory.getLogger(EapirximmunizationdatahydratorUnitTests.class);


 /*   @Test
   public void getSampleResponse() {
        logger.info("Testing Eapirximmunizationdatahydrator API");

        String expectedResponse = "{\"ID\":\"1\"}";

        String actualResponse = eapirximmunizationdatahydratorController.getResponse("1").toString();
        System.out.println("OUTPUT======" + actualResponse);
        Assert.assertEquals("Sample outcome:", expectedResponse, actualResponse);

    }*/


}
