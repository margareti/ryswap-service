package com.example.demo.userflights;

import lombok.Data;

@Data
public class SwapRequestRequest {
  private Long fromSeatId;
  private Long toSeatId;
}
