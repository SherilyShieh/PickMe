package com.petcity.pickme.common.compressor;

import java.util.ArrayList;


public interface CompressServiceListener {
    void onCompressServiceStart();
    void onCompressServiceEnd(ArrayList<LGImgCompressor.CompressResult> compressResults);
}
