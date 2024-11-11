import Carousel from "react-bootstrap/Carousel";
const AdBanner = () => {
  return (
    <Carousel>
      <Carousel.Item interval={1000}>
        <img
          src="https://mcdn.coolmate.me/image/October2022/shop-ban-giay-uy-tin-tren-shopee-296_582.jpg"
          alt="1"
          width="98%"
          height="500px"
          style={{ objectFit: "cover" }}
        />
      </Carousel.Item>
      <Carousel.Item interval={500}>
        <img
          src="https://img.pikbest.com/templates/20240729/sale-banner-decorates-shopee-shoe-shop_10688243.jpg!sw800"
          alt="1"
          width="98%"
          height="500px"
          style={{ objectFit: "cover" }}
        />
      </Carousel.Item>
      <Carousel.Item>
        <img
          src="https://mcdn.coolmate.me/image/October2022/shop-ban-giay-uy-tin-tren-shopee-296_582.jpg"
          alt="1"
          width="98%"
          height="500px"
          style={{ objectFit: "cover" }}
        />
      </Carousel.Item>
      <Carousel.Item>
        <img
          src="https://img.pikbest.com/templates/20240729/deep-discount-sale-hunting-banner-decorates-a-shoe-shop_10688247.jpg!sw800"
          alt="1"
          width="98%"
          height="500px"
          style={{ objectFit: "cover" }}
        />
      </Carousel.Item>
    </Carousel>
  );
};
export default AdBanner;
