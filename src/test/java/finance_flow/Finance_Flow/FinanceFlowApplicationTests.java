package finance_flow.Finance_Flow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FinanceFlowApplicationTests {

	@Test
	void contextLoads() {
		// Test for context loader
	}

	@Test
	void mainMethodRuns() {
		// TEST for main exceptions
		FinanceFlowApplication.main(new String[]{});
		assertThat(true).isTrue();
	}
}
