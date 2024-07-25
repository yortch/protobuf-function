variable "prefix" {
  type          = string
  description   = "Value used to prefix all resources created by this module"
  default       = "demo"
}

variable "suffix" {
  type          = string
  description   = "Value used to suffix all resources created by this module"
  default       = "protobuf"
}

variable "location" {
  type = string
  default = "East US 2"
}