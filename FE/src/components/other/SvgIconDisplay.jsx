import React from "react";

const Feature = ({ icon, title, width, height }) => (
  <img src={icon} alt={title} style={{ width: width, height: height }} />
);

const SvgIconDisplay = ({ icon, title, width = "40px", height = "40px" }) => {
  return <Feature icon={icon} title={title} width={width} height={height} />;
};

export default SvgIconDisplay;
