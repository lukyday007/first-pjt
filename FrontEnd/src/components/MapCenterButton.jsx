import React from "react";
import { Button } from "shadcn/ui";
import crosshair from "@/assets/app-icon.png";

const MapCenterButton = ({ onClick }) => {
  return (
    <Button
      onClick={onClick}
      className="rounded-full border-2 border-black p-2"
    >
      <img src={crosshair} alt="Crosshair" className="h-full w-full" />
    </Button>
  );
};

export default MapCenterButton;
