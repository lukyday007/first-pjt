import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@components/ui/Button";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogFooter,
  DialogTitle,
  DialogTrigger,
} from "@components/ui/Dialog";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuRadioGroup,
  DropdownMenuRadioItem,
  DropdownMenuTrigger,
} from "@components/ui/DropdownMenu";
import { Input } from "@components/ui/Input";
import { Label } from "@components/ui/Label";

const DropdownRadio = ({
  children,
  options,
  selectedValue,
  setSelectedValue,
}) => {
  return (
    <div>
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant="outline">{children}</Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent>
          <DropdownMenuRadioGroup
            value={selectedValue}
            onValueChange={setSelectedValue}
          >
            {options.map((option, index) => (
              <DropdownMenuRadioItem key={index} value={option}>
                {option}
              </DropdownMenuRadioItem>
            ))}
          </DropdownMenuRadioGroup>
        </DropdownMenuContent>
      </DropdownMenu>
      <p className="mt-2 text-center">{selectedValue}</p>
    </div>
  );
};

const GameSettingDialog = ({ isOpen, onClose, children }) => {
  const navigate = useNavigate();
  const [selectedCapacity, setSelectedCapacity] = useState("👪");
  const [selectedTime, setSelectedTime] = useState("⏱");
  const [selectedMapSize, setSelectedMapSize] = useState("🗺");

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogTrigger asChild>
        <Button className="mb-4 bg-theme-color-2 font-bold text-cyan-600" />
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle className="mb-2">게임 설정</DialogTitle>
          <DialogDescription>
            게임 영역의 기준점은 &quot;방장의 현재 위치&quot;입니다. <br />
            기준점으로부터 설정한 반경만큼의 원 모양으로 <br />
            게임 영역이 생성됩니다.
          </DialogDescription>
        </DialogHeader>
        <hr />
        <div className="grid grid-cols-4 items-center gap-4">
          <Label htmlFor="username" className="text-right font-bold">
            방 이름
          </Label>
          <Input id="radius" className="col-span-3" />
        </div>
        <div className="mb-4 flex justify-between">
          <DropdownRadio
            options={["4 명", "5 명", "6 명", "7 명", "8 명"]}
            selectedValue={selectedCapacity}
            setSelectedValue={setSelectedCapacity}
          >
            정원 ▼
          </DropdownRadio>
          <DropdownRadio
            options={["10 분", "15 분", "20 분", "25 분", "30 분"]}
            selectedValue={selectedTime}
            setSelectedValue={setSelectedTime}
          >
            시간 ▼
          </DropdownRadio>
          <DropdownRadio
            options={["100 m", "150 m", "200 m", "250 m", "300 m"]}
            selectedValue={selectedMapSize}
            setSelectedValue={setSelectedMapSize}
          >
            맵 사이즈 (반경) ▼
          </DropdownRadio>
        </div>

        <DialogFooter>
          <div className="flex justify-center gap-12">
            <Button
              onClick={() => navigate("/room")}
              className="w-20 bg-theme-color-1 font-bold"
            >
              시 작
            </Button>
          </div>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default GameSettingDialog;
