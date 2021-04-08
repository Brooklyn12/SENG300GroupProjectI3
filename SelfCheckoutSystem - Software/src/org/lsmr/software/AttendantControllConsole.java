package org.lsmr.software;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class AttendantControllConsole {
    private SoftwareController centre;
    public final AttendantDataBase attendantDataBase = new AttendantDataBase();
    private DispenserController dispenserController;
    private StorageUnitContoller storageUnitContoller;
    private SelfCheckoutStation station;
    private String currentAttendant = null;
    private boolean stationActive;
    public AttendantControllConsole(SoftwareController centre){
        this.centre = centre;
        this.station = centre.getStation();
        this.dispenserController = new DispenserController(station);
        this.storageUnitContoller = new StorageUnitContoller(station);
    }

    public boolean logIn(String username,String password){
        if(attendantDataBase.logIn(username,password)){
            currentAttendant = username;
            return true;
        }else{
            return false;
        }
    }

    public boolean logOut(){
        if(currentAttendant == null){
            return false;
        }else{
            currentAttendant = null;
            return true;
        }
    }




    public List<Banknote> emptyBanknoteStorage() throws SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return  storageUnitContoller.unloadBanknote();
    }

    public List<Coin> emptyCoinStorage() throws SoftwareException{
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return  storageUnitContoller.unloadCoin();
    }

    public int loadBanknote(Banknote banknote, int number)
            throws SoftwareException, OverloadException, SimulationException {
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return dispenserController.loadBanknote(banknote,number);
    }

    public BigDecimal loadCoin (Coin coin, int number)throws SoftwareException, OverloadException, SimulationException {
        if(currentAttendant == null) throw new SoftwareException("Log in required");
        return dispenserController.loadCoin(coin,number);
    }

    public void startUpStation()throws SoftwareException{
        if(currentAttendant != null){
            station.banknoteValidator.enable();
            station.banknoteInput.enable();
            station.banknoteOutput.enable();
            station.banknoteStorage.enable();
            for(CoinDispenser coinDispenser: station.coinDispensers.values()){
                coinDispenser.enable();
            }

            station.coinSlot.enable();
            station.coinTray.enable();
            station.coinStorage.enable();
            station.coinValidator.enable();
            for(BanknoteDispenser banknoteDispenser: station.banknoteDispensers.values()){
                banknoteDispenser.enable();
            }

            station.scale.enable();
            station.baggingArea.enable();
            station.cardReader.enable();
            station.handheldScanner.enable();
            station.mainScanner.enable();
            station.printer.enable();
            station.screen.enable();
            this.stationActive = true;
        }else{
            throw new SoftwareException("Log in required");
        }
    }

    public void shutDownStation()throws SoftwareException{
        if(currentAttendant != null){
            station.banknoteValidator.disable();
            station.banknoteInput.disable();
            station.banknoteOutput.disable();
            station.banknoteStorage.disable();
            for(CoinDispenser coinDispenser: station.coinDispensers.values()){
                coinDispenser.disable();
            }

            station.coinSlot.disable();
            station.coinTray.disable();
            station.coinStorage.disable();
            station.coinValidator.disable();
            for(BanknoteDispenser banknoteDispenser: station.banknoteDispensers.values()){
                banknoteDispenser.disable();
            }

            station.scale.disable();
            station.baggingArea.disable();
            station.cardReader.disable();
            station.handheldScanner.disable();
            station.mainScanner.disable();
            station.printer.disable();
            station.screen.disable();
            this.stationActive = false;
        }else{
            throw new SoftwareException("Log in required");
        }
    }
}