package com.nishantpant.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.nishantpant.OutputPrinter;
import com.nishantpant.commands.CreateParkingLotCommandExecutor;
import com.nishantpant.model.Command;
import com.nishantpant.model.ParkingLot;
import com.nishantpant.model.parking.strategy.NaturalOrderingParkingStrategy;
import com.nishantpant.service.ParkingLotService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CreateParkingLotCommandExecutorTest {
  private ParkingLotService parkingLotService;
  private OutputPrinter outputPrinter;
  private CreateParkingLotCommandExecutor createParkingLotCommandExecutor;

  @Before
  public void setUp() throws Exception {
    parkingLotService = mock(ParkingLotService.class);
    outputPrinter = mock(OutputPrinter.class);
    createParkingLotCommandExecutor =
        new CreateParkingLotCommandExecutor(parkingLotService, outputPrinter);
  }

  @Test
  public void testValidCommand() {
    assertTrue(createParkingLotCommandExecutor.validate(new Command("create_parking_lot 6")));
  }

  @Test
  public void testInvalidCommand() {
    assertFalse(createParkingLotCommandExecutor.validate(new Command("create_parking_lot")));
    assertFalse(createParkingLotCommandExecutor.validate(new Command("create_parking_lot abcd")));
  }

  @Test
  public void testCommandExecution() {
    createParkingLotCommandExecutor.execute(new Command("create_parking_lot 6"));

    final ArgumentCaptor<ParkingLot> argument = ArgumentCaptor.forClass(ParkingLot.class);
    verify(parkingLotService)
        .createParkingLot(argument.capture(), any(NaturalOrderingParkingStrategy.class));
    assertEquals(6, argument.getValue().getCapacity());
    verify(outputPrinter).printWithNewLine("Created a parking lot with 6 slots");
  }
}
