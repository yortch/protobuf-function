resource "azurerm_storage_account" "function_storage_account" {
  name                     = var.function_storage_account_name
  resource_group_name      = var.resource_group_name
  location                 = var.resource_group_location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_service_plan" "function_service_plan" {
  name                = var.function_app_service_plan_name
  location            = var.resource_group_location
  resource_group_name = var.resource_group_name
  os_type             = "Linux"
  sku_name            = "Y1"
}

resource "azurerm_linux_function_app" "function_app" {
  name                          = var.name
  location                      = var.resource_group_location
  resource_group_name           = var.resource_group_name
  service_plan_id               = azurerm_service_plan.function_service_plan.id
  storage_account_name          = azurerm_storage_account.function_storage_account.name
  storage_account_access_key    = azurerm_storage_account.function_storage_account.primary_access_key

  site_config {
  }
}
