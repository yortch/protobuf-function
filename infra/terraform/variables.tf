variable "prefix" {
  type          = string
  description   = "Value used to prefix all resources created by this module"
  default       = "protobuf"
}

variable "suffix" {
  type          = string
  description   = "Value used to suffix all resources created by this module"
}

variable "location" {
  type = string
  default = "East US 2"
}