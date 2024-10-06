package com.skillbox.controller;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.controller.option.MainMenuOption;
import com.skillbox.data.model.Analytic;
import com.skillbox.data.repository.AnalyticRepository;
import com.skillbox.service.TransactionService;

/**
 * Консольный контроллер для управления навигацией по главному меню.
 */
public class MainMenuController extends AbstractMenuController<MainMenuOption> {

    private final TransactionService transactionService;
    private final AnalyticRepository saver;
    private final GroupMenuController analyticsMenuController;
    private final AggregateMenuController aggregateMenuController;
    private final SearchMenuController searchMenuController;

    public MainMenuController(TransactionService transactionService, AnalyticRepository saver) {
        super(MainMenuOption.class, "Анализ финансов");
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
            MainMenuOption i = selectMenu();
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
                        System.err.println("Необходимо сначала рассчитать аналитику");
                        break;
                    }
                    saver.save(analytics);
                    break;
                case EXIT:
                    return;
            }
        }
    }
}
