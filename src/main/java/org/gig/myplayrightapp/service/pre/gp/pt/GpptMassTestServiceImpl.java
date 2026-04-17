package org.gig.myplayrightapp.service.pre.gp.pt;

import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.service.AbstractMassTestService;
import org.gig.myplayrightapp.util.ExcelReportUtil;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GpptMassTestServiceImpl extends AbstractMassTestService implements GpptMassTestService {

    private final GoldenParkPtTestService goldenParkPtTestService;

    public GpptMassTestServiceImpl(
            GoldenParkPtTestService goldenParkPtTestService,
            ExcelReportUtil excelReportUtil,
            PlaywrightUtil playwrightUtil) {
        super(excelReportUtil, playwrightUtil);
        this.goldenParkPtTestService = goldenParkPtTestService;
    }

    @Override protected String screenshotDir()    { return "screenshots/gp/"; }
    @Override protected String reportTitle()      { return "Golden Park Portugal Automated Test Report"; }
    @Override protected String environmentLabel() { return "GP-PT"; }

    @Override
    public byte[] runAllGpptTestsAndGenerateReport() {
        return runAndGenerateReport();
    }

    @Override
    protected List<TestDefinition> buildTestDefinitions() {
        return List.of(
                new TestDefinition("GPPT-TC-001", "Manual registration (happy path)",
                        goldenParkPtTestService::runManualRegistrationTest),
                new TestDefinition("GPPT-TC-002", "Duplicate NIF validation",
                        goldenParkPtTestService::runDuplicateNifTest)
        );
    }
}
