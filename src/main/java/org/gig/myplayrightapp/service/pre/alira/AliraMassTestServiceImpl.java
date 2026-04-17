package org.gig.myplayrightapp.service.pre.alira;

import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.service.AbstractMassTestService;
import org.gig.myplayrightapp.service.pre.alira.dashboard.AliraNavigateTabGamesService;
import org.gig.myplayrightapp.service.pre.alira.dashboard.AliraNavigateTabMarketingService;
import org.gig.myplayrightapp.service.pre.alira.dashboard.AliraNavigateTabWebsiteService;
import org.gig.myplayrightapp.util.ExcelReportUtil;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AliraMassTestServiceImpl extends AbstractMassTestService implements AliraMassTestService {

    private final AliraTestService aliraTestService;
    private final AliraNavigateTabGamesService gamesService;
    private final AliraNavigateTabWebsiteService websiteService;
    private final AliraNavigateTabMarketingService marketingService;

    public AliraMassTestServiceImpl(
            AliraTestService aliraTestService,
            AliraNavigateTabGamesService gamesService,
            AliraNavigateTabWebsiteService websiteService,
            AliraNavigateTabMarketingService marketingService,
            ExcelReportUtil excelReportUtil,
            PlaywrightUtil playwrightUtil) {
        super(excelReportUtil, playwrightUtil);
        this.aliraTestService = aliraTestService;
        this.gamesService = gamesService;
        this.websiteService = websiteService;
        this.marketingService = marketingService;
    }

    @Override protected String screenshotDir()    { return "screenshots/alira/"; }
    @Override protected String reportTitle()      { return "Alira Automated Test Report"; }
    @Override protected String environmentLabel() { return "Alira"; }

    @Override
    public byte[] runAllAliraTestsAndGenerateReport() {
        return runAndGenerateReport();
    }

    @Override
    protected List<TestDefinition> buildTestDefinitions() {
        return List.of(
                new TestDefinition("testCase001", "Login to Alira Back Office",
                        aliraTestService::testCase001LoginTest),
                new TestDefinition("testCase002", "Navigate to Player Profile",
                        aliraTestService::testCase002NavigatePlayerProfile),
                new TestDefinition("testCase003", "Navigate → Games → Rooms",
                        gamesService::testCase003NavigateTabGamesRoomsTest),
                new TestDefinition("testCase004", "Navigate → Games → Process Rooms",
                        gamesService::testCase004NavigateTabGamesProcessRoomsTest),
                new TestDefinition("testCase005", "Navigate → Games → Lobby Rooms",
                        gamesService::testCase005NavigateTabGamesLobbyRoomsTest),
                new TestDefinition("testCase006", "Navigate → Games → Providers",
                        gamesService::testCase006NavigateTabGamesProvidersTest),
                new TestDefinition("testCase007", "Navigate → Games → Themes & Tags",
                        gamesService::testCase007NavigateTabGamesThemesTagsTest),
                new TestDefinition("testCase008", "Navigate → Games → Exchange Profile",
                        gamesService::testCase008NavigateTabGamesExchangeProfileTest),
                new TestDefinition("testCase009", "Navigate → Website → CMS",
                        websiteService::testCase009NavigateTabWebsiteCMSTest),
                new TestDefinition("testCase010", "Navigate → Website → Configuration → CMS Access",
                        websiteService::testCase010NavigateTabWebsiteConfigurationCMSAccessTest),
                new TestDefinition("testCase011", "Navigate → Website → Configuration → Constants",
                        websiteService::testCase011NavigateTabWebsiteConfigurationConstantsTest),
                new TestDefinition("testCase012", "Navigate → Dashboard — verify table has data",
                        marketingService::testCase012NavigateDashboardTest),
                new TestDefinition("testCase013", "Navigate → Marketing → Bonus → Deposit Promotions — verify table has data",
                        marketingService::testCase013NavigateMarketingBonusDepositPromotionsTest),
                new TestDefinition("testCase014", "Navigate → Marketing → Bonus → Deposit Promotions → click New — verify modal",
                        marketingService::testCase014NavigateMarketingBonusDepositPromotionsNewModalTest),
                new TestDefinition("testCase015", "Create a new Deposit Promotion",
                        marketingService::testCase015CreateDepositPromotionTest),
                new TestDefinition("testCase016", "Edit the first Deposit Promotion and verify update",
                        marketingService::testCase016EditDepositPromotionTest),
                new TestDefinition("testCase017", "Delete the first Deposit Promotion and verify removal",
                        marketingService::testCase017DeleteDepositPromotionTest)
        );
    }
}
