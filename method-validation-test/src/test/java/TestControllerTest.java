
import static org.easymock.EasyMock.*;

import javax.validation.ConstraintValidatorFactory;

import org.easymock.EasyMock;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.vkviatkouski.sample.context.ContentMethodValidationPostProcessor;
import com.vkviatkouski.sample.rest.RestBusinessException;
import com.vkviatkouski.sample.rest.TestController;
import com.vkviatkouski.sample.service.IService;
import com.vkviatkouski.sample.validation.ChannelExistsValidator;


public class TestControllerTest {
	private ConstraintValidatorFactory factory;
	
	private IService service;

	@BeforeMethod
	public void setup() {
		factory = EasyMock.createMock(ConstraintValidatorFactory.class);
		service = EasyMock.createMock(IService.class);

		ChannelExistsValidator validator = new ChannelExistsValidator();
		validator.setService(service);

		expect(factory.getInstance(ChannelExistsValidator.class)).andReturn(validator).anyTimes();
		expect(service.exists("100")).andReturn(true);
		expect(service.exists("101")).andReturn(false);
		replay(factory, service);
	}

	@Test
	public void test1() {
		LocalValidatorFactoryBean validationFactory = new LocalValidatorFactoryBean();
		validationFactory.setConstraintValidatorFactory(factory);
		validationFactory.afterPropertiesSet();
		ContentMethodValidationPostProcessor processor = new ContentMethodValidationPostProcessor();
		processor.setValidator(validationFactory.getValidator());
		processor.afterPropertiesSet();
		TestController controller = (TestController) processor.postProcessAfterInitialization(new TestController(), "");
		Assert.assertNotNull(controller);
		
		Assert.assertEquals(controller.result("100"), "Hello 100");
		try {
			controller.result("101");
			Assert.fail();
		} catch (RestBusinessException rbe) {
			// supposed to be here
		}
	}
}
