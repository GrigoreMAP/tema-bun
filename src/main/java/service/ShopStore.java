package service;


import service.customers.CustomersService;
import service.producators.ProducatorsService;
import service.produs.InventoryService;
import service.reports.ReportsService;
import service.shopping_cart.ShoppingCartService;

public class ShopStore {
    private InventoryService inventoryService;
    private ShoppingCartService shoppingCartService;
    private ProducatorsService producatorsService;
    private CustomersService customersService;

    private ReportsService reportsService;

    public ShopStore(InventoryService inventoryService, ShoppingCartService shoppingCartService, ProducatorsService producatorsService, CustomersService customersService, ReportsService reportsService) {
        this.inventoryService = inventoryService;
        this.shoppingCartService = shoppingCartService;
        this.producatorsService = producatorsService;
        this.customersService = customersService;
        this.reportsService = reportsService;
    }

    public InventoryService getInventoryService() {
        return this.inventoryService;
    }

    public ShoppingCartService getShoppingCartService() {
        return this.shoppingCartService;
    }

    public ProducatorsService getProducatorsService() {
        return this.producatorsService;
    }

    public CustomersService getCustomersService() {
        return this.customersService;
    }

    public ReportsService getReportsService() {
        return this.reportsService;
    }
}


