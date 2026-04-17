package org.gig.myplayrightapp.service.pre.cgm;

import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.service.AbstractMassTestService;
import org.gig.myplayrightapp.util.ExcelReportUtil;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CgmMassTestServiceImpl extends AbstractMassTestService implements CgmMassTestService {

    private final CasinoGranMadridTestService casinoGranMadridTestService;

    public CgmMassTestServiceImpl(
            CasinoGranMadridTestService casinoGranMadridTestService,
            ExcelReportUtil excelReportUtil,
            PlaywrightUtil playwrightUtil) {
        super(excelReportUtil, playwrightUtil);
        this.casinoGranMadridTestService = casinoGranMadridTestService;
    }

    @Override protected String screenshotDir()    { return "screenshots/cgm/"; }
    @Override protected String reportTitle()      { return "Casino Gran Madrid Automated Test Report"; }
    @Override protected String environmentLabel() { return "CGM"; }

    @Override
    public byte[] runAllCgmTestsAndGenerateReport() {
        return runAndGenerateReport();
    }

    @Override
    protected List<TestDefinition> buildTestDefinitions() {
        return List.of(
                new TestDefinition("CGM-TC-001", "Pre-production site access",
                        casinoGranMadridTestService::testCase001RunPreProdAccessTest),
                new TestDefinition("CGM-TC-002", "Register button opens modal",
                        casinoGranMadridTestService::testCase002RunRegisterButtonTest),
                new TestDefinition("CGM-TC-003", "Fast registration via Veridas (camera prompt)",
                        casinoGranMadridTestService::testCase003RunFastRegisterVeridasTest),
                new TestDefinition("CGM-TC-004", "Manual registration (happy path)",
                        casinoGranMadridTestService::testCase004RunManualRegisterTest),
                new TestDefinition("CGM-TC-005", "Manual registration validation (missing name)",
                        casinoGranMadridTestService::testCase005RunInvalidNameRegistrationTest),
                new TestDefinition("CGM-TC-006", "Manual registration with duplicated DNI/NIE",
                        casinoGranMadridTestService::testCase006RunDuplicatedRegisterTest)
        );
    }
}
