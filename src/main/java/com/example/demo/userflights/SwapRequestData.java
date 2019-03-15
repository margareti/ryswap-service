package com.example.demo.userflights;

import com.example.demo.userflights.swaprequest.SwapRequest;
import com.example.demo.userflights.swaprequest.SwapRequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class SwapRequestData {

  private SwapRequest swapRequest;
  private Boolean incoming;

  @Builder
  public SwapRequestData(SwapRequest swapRequest, Boolean incoming) {
    this.incoming = incoming;
    this.swapRequest = swapRequest;
  }


}
