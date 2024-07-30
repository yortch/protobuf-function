output "azure_function_id" {
    description = "The URLs of the app services created."
    value       = azurerm_linux_function_app.function_app.*.id
}
output "azure_function_url" {
    description = "The resource ids of the app services created."
    value       = azurerm_linux_function_app.function_app.*.default_hostname
}
output "app_service_type" {
    description = "The type of app service created."
    value       = azurerm_linux_function_app.function_app.*.kind
}
output "azure_function_app_name" {
    description = "The name of app function created."
    value       = azurerm_linux_function_app.function_app.name
}