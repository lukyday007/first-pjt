import React from "react";
import { Button } from "@/components/ui/Button";
import giveUpIcon from "@/assets/gameplay-icon/giveup-button.svg";

const GiveUpButton = () => {
  return (
    <div>
      <Button
        variant="outline"
        className="m-1 h-[8vh] w-[8vh] rounded-full border-2 border-black bg-white text-red-600 shadow-xl hover:bg-red-600 hover:text-white active:bg-red-800 active:text-white"
      >
        <img src={giveUpIcon} alt="Give Up" className="h-full w-full" />
      </Button>
    </div>
  );
};

export default GiveUpButton;
