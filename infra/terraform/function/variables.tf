variable "resource_group_name" {
  type = string
  description = "The name of the resource group"
}

variable "resource_group_location" {
  type = string
  description = "The location of the resource group"
}

variable "name" {
  type = string
  description = "The name of the function app"
}

variable "service_name" {
  type = string
  description = "The service name"
  default = "protobuf"
}
variable "function_storage_account_name" {
  type = string
  description = "The name of the storage account for the function app"  
}

variable "function_app_service_plan_name" {
  type = string
  description = "The name of the app service plan for the function app"
}

variable "function_worker_runtime" {
  description = "Functions worker runtime"
  type = string
  default = "java"
}