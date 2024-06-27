package com.quran.tester;

import com.quran.tester.common.config.TracingConfig;
import com.quran.tester.quranTester.QuranTesterServiceImpl;
import com.quran.tester.quranTester.quranCloud.Config.QuranCloudProperties;
import com.quran.tester.quranTester.quranCloud.Config.QuranCloudWebClientConfig;
import com.quran.tester.quranTester.quranCloud.QuranCloudServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({QuranTesterServiceImpl.class, QuranCloudServiceImpl.class, QuranCloudWebClientConfig.class, TracingConfig.class, WebClientAutoConfiguration.class, QuranCloudProperties.class})
class TesterApplicationTests {

	@Test
	void contextLoads() {
	}

}
