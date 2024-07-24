import { Button } from "@components/ui/Button";
import { useNavigate } from "react-router-dom";

import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogFooter,
  DialogTitle,
  DialogTrigger,
} from "@components/ui/Dialog";

import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@components/ui/Table";

import { Input } from "@components/ui/Input";
import { Label } from "@components/ui/Label";
import { RadioGroup, RadioGroupItem } from "@components/ui/RadioGroup";

const Room = () => {
  const navigate = useNavigate();

  return (
    <>
      <div className="flex h-screen flex-col items-center justify-center bg-white">
        {/* 게임설정 다이얼로그 */}
        <Dialog>
          <DialogTrigger asChild>
            <Button className="mb-4 bg-theme-color-2 font-bold text-cyan-600">
              모집 완료!
            </Button>
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
            <div className="grid gap-4 py-4">
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="name" className="text-right font-bold">
                  시간 (분)
                </Label>
                <RadioGroup defaultValue="option-one" className="flex flex-row">
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="option-one" id="option-one" />
                    <Label htmlFor="option-one">10</Label>
                  </div>
                  |
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="option-two" id="option-two" />
                    <Label htmlFor="option-two">15</Label>
                  </div>
                  |
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="option-three" id="option-three" />
                    <Label htmlFor="option-three">20</Label>
                  </div>
                  |
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="option-four" id="option-four" />
                    <Label htmlFor="option-four">25</Label>
                  </div>
                </RadioGroup>
              </div>
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="username" className="text-right font-bold">
                  반경 (m)
                </Label>
                <Input id="radius" defaultValue="100" className="col-span-3" />
              </div>
            </div>
            <DialogFooter>
              <div className="flex justify-center gap-12">
                <Button
                  onClick={() => navigate("/game-play")}
                  className="bg-theme-color-1 font-bold"
                >
                  시작
                </Button>
                <DialogClose asChild>
                  <Button
                    type="button"
                    variant="secondary"
                    className="bg-rose-600 font-bold text-white"
                  >
                    취소
                  </Button>
                </DialogClose>
              </div>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        {/* 랜덤 생성 QR code */}

        {/* 랜덤 생성 방 번호 */}

        {/* 현재 참가자 목록 */}
      </div>
    </>
  );
};

export default Room;
