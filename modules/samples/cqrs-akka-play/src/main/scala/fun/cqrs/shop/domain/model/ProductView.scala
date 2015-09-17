package fun.cqrs.shop.domain.model

import play.api.libs.json.Json

case class ProductView(name: String, description: String, price: Double, identifier: ProductId)

object ProductView {

  implicit val format = Json.writes[ProductView]
}
