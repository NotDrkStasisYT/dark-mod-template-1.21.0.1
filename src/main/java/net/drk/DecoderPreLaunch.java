package net.drk;

import net.drk.encryption.item.ITEMBase64Decoder;
import net.drk.encryption.model.MODELBase64Decoder;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class DecoderPreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        initializeDecoder();
    }

    private void initializeDecoder() {
        System.out.println("Starting early asset decoding...");

        try {
            decodeAssets();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Asset decoding complete.");
    }

    private void decodeAssets() {
        ITEMBase64Decoder itemDecoder = new ITEMBase64Decoder();
        itemDecoder.decodeDirectory(
                "C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\encrypted\\textures\\item",
                "C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\assets\\drkmod\\textures\\item"
        );

        // Decode models
        MODELBase64Decoder modelDecoder = new MODELBase64Decoder();
        modelDecoder.decodeDirectory(
                "C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\encrypted\\models\\item",
                "C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\assets\\drkmod\\models\\item"
        );

        // Additional decoding logic can be added here if necessary
    }
}
