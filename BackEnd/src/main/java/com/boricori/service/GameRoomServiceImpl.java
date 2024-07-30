package com.boricori.service;

import com.boricori.dto.request.gameroom.GameRequest;
import com.boricori.dto.request.gameroom.StartGameRoomRequest;
import com.boricori.dto.response.gameroom.CreateGameRoomResponse;
import com.boricori.dto.response.gameroom.GameRoomSettingResponse;
import com.boricori.entity.GameRoom;
import com.boricori.repository.GameRoomRepo.GameRoomRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class GameRoomServiceImpl implements GameRoomService {

  private static final Logger log = LoggerFactory.getLogger(GameRoomServiceImpl.class);
  @Autowired
  private GameRoomRepository gameRoomRepository;

@Override
  public GameRoom findGame(Long id){
    return gameRoomRepository.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public CreateGameRoomResponse createRoom(GameRequest gameRoomInfo) throws IOException, WriterException {
    GameRoom gameRoom = GameRoom.builder().gameRoomRequest(gameRoomInfo).build();
    String currTime = String.valueOf(System.currentTimeMillis());
    String code = currTime.substring(currTime.length() - 8, currTime.length());
    gameRoom.setCodeNumber(code);
    gameRoom = gameRoomRepository.save(gameRoom);
    String roomUrl = "http://runtail/join-room/" + gameRoom.getId();
    String qrCode = generateQRCodeImage(roomUrl);
    gameRoom.createQrCode(qrCode);
    CreateGameRoomResponse response = new CreateGameRoomResponse(gameRoom.getId(), qrCode,
        gameRoom.getCodeNumber());
    return response;
  }

  @Override
  @Transactional
  public GameRoom updateRoom(Long id, StartGameRoomRequest request) {
    GameRoom gameRoom = gameRoomRepository.findById(id).orElseThrow(IllegalAccessError::new);
    gameRoom.startGameTime();
    gameRoom.setCenter(request.getCenterLat(), request.getCenterLng());
    return gameRoom;
  }

  private String generateQRCodeImage(String text) throws IOException, WriterException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
    byte[] pngData = pngOutputStream.toByteArray();
    return Base64.getEncoder().encodeToString(pngData);
  }
}
