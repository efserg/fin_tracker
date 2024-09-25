package com.skillbox.console;

import com.skillbox.console.dto.AggregateOption;
import com.skillbox.console.dto.GroupOption;
import com.skillbox.console.dto.TransactionFilterDto;
import com.skillbox.console.dto.Analytic;
import com.skillbox.data.AnalyticSaver;
import com.skillbox.service.TransactionService;
import java.util.Set;

/**
 * Консольный контроллер для управления навигацией по главному меню.
 */
public class MainMenuController extends AbstractMenuController {

    private static final int SEARCH_CRITERIA = 1;
    private static final int GROUP_OPTION = 2;
    private static final int AGGREGATION_METHOD = 3;
    private static final int CALCULATE_ANALYTICS = 4;
    private static final int SAVE_ANALYTICS = 5;
    public static final Set<Integer> OPTIONS = Set.of(SEARCH_CRITERIA, GROUP_OPTION, AGGREGATION_METHOD, CALCULATE_ANALYTICS, SAVE_ANALYTICS);

    private final TransactionService transactionService;
    private final AnalyticSaver saver;
    private final GroupMenuController analyticsMenuController;
    private final AggregateMenuController aggregateMenuController;
    private final SearchMenuController searchMenuController;

    public MainMenuController(TransactionService transactionService, AnalyticSaver saver) {
        super(OPTIONS);
        this.transactionService = transactionService;
        this.saver = saver;
        this.analyticsMenuController = new GroupMenuController();
        this.searchMenuController = new SearchMenuController();
        this.aggregateMenuController = new AggregateMenuController();
    }

    public void start() {
        goMainMenu();
    }

    private void goMainMenu() {
        TransactionFilterDto transactionFilter = new TransactionFilterDto();
        GroupOption groupOption = GroupOption.WITHOUT_GROUPING;
        AggregateOption aggregateOption = AggregateOption.SUM;
        Analytic analytics = null;
        while (true) {
            int i = selectMenu();
            switch (i) {
                case SEARCH_CRITERIA:
                    transactionFilter = searchMenuController.getTransactionFilter();
                    break;
                case GROUP_OPTION:
                    groupOption = analyticsMenuController.getGroupOption();
                    break;
                case AGGREGATION_METHOD:
                    aggregateOption = aggregateMenuController.getAggregateOption();
                    break;
                case CALCULATE_ANALYTICS:
                    analytics = transactionService.calculateAnalytics(transactionFilter,
                            groupOption, aggregateOption);
                    System.out.println(analytics);
                    break;
                case SAVE_ANALYTICS:
                    if (analytics == null) {
                        System.out.println("Необходимо рассчитать аналитику");
                        break;
                    }
                    saver.save(analytics);
                    break;
                case EXIT_OPTION:
                    return;
            }
        }
    }

    @Override
    protected String getMenuDescription() {
        return "\nАнализ финансов\n"
                + "\n"
                + SEARCH_CRITERIA + " – задать критерии поиска транзакций\n"
                + GROUP_OPTION + " – выбрать опцию группировки\n"
                + AGGREGATION_METHOD + " – выбрать способ агрегации\n"
                + CALCULATE_ANALYTICS + " – рассчитать и вывести аналитику\n"
                + SAVE_ANALYTICS + " – сохранить аналитику\n";
    }
}
