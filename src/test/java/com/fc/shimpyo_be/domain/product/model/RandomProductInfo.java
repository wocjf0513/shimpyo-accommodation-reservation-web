package com.fc.shimpyo_be.domain.product.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RandomProductInfo {

    private static final ArrayList<String> imageUrl = new ArrayList<String>(
        List.of("https://i.imgur.com/5f7YkbGundefined.jpg",
            "https://i.imgur.com/ypWy67fundefined.jpg", "https://i.imgur.com/XNuijr4undefined.jpg",
            "https://i.imgur.com/2uStphxundefined.jpg", "https://i.imgur.com/3ecmXL9undefined.jpg",
            "https://i.imgur.com/bhGm4xcundefined.jpg", "https://i.imgur.com/Ms6PScNundefined.jpg",
            "https://i.imgur.com/RFJc2nOundefined.jpg", "https://i.imgur.com/aLyBnOQundefined.jpg",
            "https://i.imgur.com/FDpLDFAundefined.jpg", "https://i.imgur.com/Wmlh4U8undefined.jpg",
            "https://i.imgur.com/geuIuvUundefined.jpg", "https://i.imgur.com/SupINjzundefined.jpg",
            "https://i.imgur.com/kMeAwLyundefined.jpg", "https://i.imgur.com/5oSAki6undefined.jpg",
            "https://i.imgur.com/fVMuRq7undefined.jpg", "https://i.imgur.com/IbFPcuJundefined.jpg",
            "https://i.imgur.com/oVWnAkAundefined.jpg", "https://i.imgur.com/d8ADGx3undefined.jpg",
            "https://i.imgur.com/pPzKSXOundefined.jpg", "https://i.imgur.com/D2XuXw0undefined.jpg",
            "https://i.imgur.com/Fsv4u9Rundefined.jpg", "https://i.imgur.com/KmDYErAundefined.jpg",
            "https://i.imgur.com/B3FKa1iundefined.jpg", "https://i.imgur.com/vIKUVAiundefined.jpg",
            "https://i.imgur.com/I660vSmundefined.jpg", "https://i.imgur.com/RVrtMhsundefined.jpg",
            "https://i.imgur.com/kVSDah6undefined.jpg", "https://i.imgur.com/Xix7a9Iundefined.jpg",
            "https://i.imgur.com/tFefJpeundefined.jpg", "https://i.imgur.com/Q34HZGvundefined.jpg",
            "https://i.imgur.com/19Zh98hundefined.jpg", "https://i.imgur.com/YNacxCCundefined.jpg",
            "https://i.imgur.com/FswZuqFundefined.jpg", "https://i.imgur.com/2biPgGrundefined.jpg",
            "https://i.imgur.com/X1u5qsuundefined.jpg", "https://i.imgur.com/yrikS2sundefined.jpg",
            "https://i.imgur.com/tGgYdTqundefined.jpg", "https://i.imgur.com/Gazf7Ilundefined.jpg",
            "https://i.imgur.com/YUxBgDdundefined.jpg", "https://i.imgur.com/1wFewecundefined.jpg",
            "https://i.imgur.com/CvEq3tpundefined.jpg", "https://i.imgur.com/hUJ7uFcundefined.jpg",
            "https://i.imgur.com/JKaU1Mcundefined.jpg", "https://i.imgur.com/IRMXAHiundefined.jpg",
            "https://i.imgur.com/CBzMprcundefined.jpg", "https://i.imgur.com/wXvLZwzundefined.jpg",
            "https://i.imgur.com/gJkHNu8undefined.jpg", "https://i.imgur.com/NgK3ECVundefined.jpg",
            "https://i.imgur.com/ZwwlngHundefined.jpg", "https://i.imgur.com/3wGDcEKundefined.jpg",
            "https://i.imgur.com/pvi6RXXundefined.jpg", "https://i.imgur.com/PpThiOYundefined.jpg",
            "https://i.imgur.com/cZwBUshundefined.jpg", "https://i.imgur.com/nIa4RQkundefined.jpg",
            "https://i.imgur.com/oQKtQIVundefined.jpg", "https://i.imgur.com/5Uc4wJdundefined.jpg",
            "https://i.imgur.com/5XRGjp0undefined.jpg", "https://i.imgur.com/yJat5NAundefined.jpg",
            "https://i.imgur.com/t95AQNAundefined.jpg", "https://i.imgur.com/b1zbiVZundefined.jpg",
            "https://i.imgur.com/AJJNOiqundefined.jpg", "https://i.imgur.com/r0iH1woundefined.jpg",
            "https://i.imgur.com/X7XD2Ouundefined.jpg", "https://i.imgur.com/TfuJOldundefined.jpg",
            "https://i.imgur.com/Q9aKe57undefined.jpg", "https://i.imgur.com/EkUvgeXundefined.jpg",
            "https://i.imgur.com/vOWAHHVundefined.jpg", "https://i.imgur.com/9RB0G96undefined.jpg",
            "https://i.imgur.com/5loLXGfundefined.jpg", "https://i.imgur.com/ZuiaH2Lundefined.jpg",
            "https://i.imgur.com/ooLwVRMundefined.jpg", "https://i.imgur.com/jRCZHBwundefined.jpg",
            "https://i.imgur.com/d0DumjPundefined.jpg", "https://i.imgur.com/pN1Rbmlundefined.jpg",
            "https://i.imgur.com/vMWyy8fundefined.jpg", "https://i.imgur.com/dODWq84undefined.jpg",
            "https://i.imgur.com/SQmh5wyundefined.jpg", "https://i.imgur.com/FQBjPobundefined.jpg",
            "https://i.imgur.com/P92be05undefined.jpg", "https://i.imgur.com/18BhxKNundefined.jpg",
            "https://i.imgur.com/pAYEC6Pundefined.jpg", "https://i.imgur.com/ZjXSEE9undefined.jpg",
            "https://i.imgur.com/aJQFjAoundefined.jpg", "https://i.imgur.com/2h491a1undefined.jpg",
            "https://i.imgur.com/RlFFIcQundefined.jpg", "https://i.imgur.com/8onoZuyundefined.jpg",
            "https://i.imgur.com/noIhjlVundefined.jpg", "https://i.imgur.com/bZXJv29undefined.jpg",
            "https://i.imgur.com/lfSox2Pundefined.jpg", "https://i.imgur.com/lBI9fVYundefined.jpg",
            "https://i.imgur.com/sWRk8STundefined.jpg", "https://i.imgur.com/9kRgcF0undefined.jpg",
            "https://i.imgur.com/h10j2ALundefined.jpg", "https://i.imgur.com/vzJxgF2undefined.jpg",
            "https://i.imgur.com/k9GXZJWundefined.png", "https://i.imgur.com/PZU2ge5undefined.jpg",
            "https://i.imgur.com/UYW0Q4Qundefined.jpg", "https://i.imgur.com/Bnw1LYjundefined.jpg",
            "https://i.imgur.com/3Yy09zzundefined.jpg", "https://i.imgur.com/OTnCqJ4undefined.jpg",
            "https://i.imgur.com/TzQpczfundefined.jpg",
            "https://i.imgur.com/gVVo54bundefined.jpg"));


    private static final ArrayList<String> descriptions = new ArrayList<String>(
        List.of("해수욕장과 가까운 위치, 깨끗하고 쾌적한 시설, 편리한 주변 편의 시설", "바다뷰와 깨끗한 내부 구조로 이루어져 있습니다.",
            "해변과 가까운 위치에 위치한 깨끗하고 쾌적한 숙소입니다.", "속초해수욕장과 가깝고 이마트 등 편의시설이 가까워 편리한 위치에 있는 호텔입니다.",
            "위치도 좋고 시설도 깔끔하며, 자주 이용하는 곳으로 만족도가 높은 편입니다. ",
            "해당 숙소는 깔끔하고 친절한 직원들이 있으며, 쾌적하고 조용한 분위기로 여행객들이 편하게 쉴 수 있는 곳입니다. ",
            "굉장히 깨끗하고 편안한 숙소입니다.", "깔끔하고 조용한 분위기에서 편안한 휴식을 취할 수 있는 것으로 나타났습니다. "));


    public static String genRandomImage() {
        return imageUrl.get(ThreadLocalRandom.current().nextInt(imageUrl.size()));
    }

    public static String genRandomDescription() {
        return descriptions.get(ThreadLocalRandom.current().nextInt(descriptions.size()));
    }

}
