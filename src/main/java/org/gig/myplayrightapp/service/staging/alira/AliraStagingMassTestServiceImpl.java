package org.gig.myplayrightapp.service.staging.alira;

import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.service.AbstractMassTestService;
import org.gig.myplayrightapp.util.ExcelReportUtil;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AliraStagingMassTestServiceImpl extends AbstractMassTestService implements AliraStagingMassTestService {

    private final AliraStagingTestService aliraStagingTestService;

    public AliraStagingMassTestServiceImpl(
            AliraStagingTestService aliraStagingTestService,
            ExcelReportUtil excelReportUtil,
            PlaywrightUtil playwrightUtil) {
        super(excelReportUtil, playwrightUtil);
        this.aliraStagingTestService = aliraStagingTestService;
    }

    @Override protected String screenshotDir()    { return "screenshots/staging/"; }
    @Override protected String reportTitle()      { return "Alira Staging Automated Test Report"; }
    @Override protected String environmentLabel() { return "Staging"; }

    @Override
    public byte[] runAllStagingTestsAndGenerateReport() {
        return runAndGenerateReport();
    }

    @Override
    protected List<TestDefinition> buildTestDefinitions() {
        return List.of(
                new TestDefinition("NL2-TC-001", "Login to Alira Staging Back Office",
                        aliraStagingTestService::testCase001LoginTest),
                new TestDefinition("NL2-TC-002", "Navigate to Staging Player Profile",
                        aliraStagingTestService::testCase002NavigatePlayerProfile),
                new TestDefinition("NL2-TC-003", "Website → CMS → Banners → edit and restore Banner Landscape 1",
                        aliraStagingTestService::testCase003CmsWebsiteEditBanner)
        );
    }
}
