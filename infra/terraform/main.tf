locals {
}

terraform {
  backend "local" {}

  required_providers {
    azurerm = {
      source = "hashicorp/azurerm"
      version = "3.99.0"

    }
  }
}

provider "azurerm" {
  features {
  }
}

resource "azurerm_resource_group" "resource_group" {
  name     = "rg-${var.prefix}-${var.suffix}"
  location = "${var.location}"
}

module "protobuf_function" {
  source = "./function"

  resource_group_name = azurerm_resource_group.resource_group.name
  resource_group_location = azurerm_resource_group.resource_group.location
  name = "fn-${var.prefix}-${var.suffix}"
  function_storage_account_name = "stfunc${var.prefix}${var.suffix}"
  function_app_service_plan_name = "fn-${var.prefix}-${var.suffix}"
}