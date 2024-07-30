
resource "azurerm_log_analytics_workspace" "function_log_analytics_workspace" {
  name                = var.log_analytics_name
  location            = var.resource_group_location
  resource_group_name = var.resource_group_name
}

resource "azurerm_application_insights" "function_app_insights" {
  name                = var.application_insights_name
  location            = var.resource_group_location
  resource_group_name = var.resource_group_name
  application_type    = "web"
  workspace_id        = azurerm_log_analytics_workspace.function_log_analytics_workspace.id
}

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

  app_settings = {
    FUNCTIONS_WORKER_RUNTIME              = var.function_worker_runtime,
    APPLICATIONINSIGHTS_ENABLE_AGENT      = "true",
    WEBSITE_RUN_FROM_PACKAGE              = "1",
    APPLICATIONINSIGHTS_CONNECTION_STRING = azurerm_application_insights.function_app_insights.connection_string,
    APPINSIGHTS_INSTRUMENTATIONKEY        = azurerm_application_insights.function_app_insights.instrumentation_key
  }

  tags = {
    azd-service-name = var.service_name
  }

  site_config {
    application_stack {
      java_version = "17"
    }
  }
}
