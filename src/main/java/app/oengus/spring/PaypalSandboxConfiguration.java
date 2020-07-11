package app.oengus.spring;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class PaypalSandboxConfiguration {

	@Value("${paypal.clientId}")
	private String clientId;

	@Value("${paypal.clientSecret}")
	private String clientSecret;

	@Bean
	public PayPalHttpClient payPalHttpClient() {
		final PayPalEnvironment payPalEnvironment = new PayPalEnvironment.Sandbox(this.clientId, this.clientSecret);
		return new PayPalHttpClient(payPalEnvironment);
	}

}
