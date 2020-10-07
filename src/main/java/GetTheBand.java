import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GetTheBand {

    private String bandList = "220 Volt,44 Magnum,A II Z,Accept,AC/DC,Aerosmith,Alice Cooper,Angel,Ángeles del Infierno,Angel Witch," +
            "Anthem,Anthrax,Anvil,Armageddon,Atomic Mass,Atomic Rooster,Atomkraft,Attila,Axe,Babe Ruth,Bang,Barnabas,Barón Rojo,Battleaxe,Bengal Tigers," +
            "Beowülf,Birth Control,Bitch,Black Death,Black 'n Blue,Black Rose,Black Sabbath,Black Widow,Bleak House,Blind Illusion,Blitzkrieg,Bloodrock," +
            "Blue Cheer,Blue Öyster Cult,Bodine,Boss,Bow Wow,Bronz,Edgar Broughton Band,Budgie,Buffalo,Bulldozer,Cactus,Captain Beyond," +
            "Chateaux,Cirith Ungol,Cloven Hoof,Coven,Crimson Glory,Crushed Butler,Death SS,Dedringer,Deep Machine,Deep Purple,Def Leppard,Demon,The Deviants," +
            "Diamond Head,Die Krupps,Divlje Jagode,Dokken,Doro,Dust,Earthshaker,Easy Action,E.F. Band,Electric Sun,Elf,Ethel the Frog,Europe,Exciter,Fallout,Fist," +
            "Flotsam and Jetsam,Flower Travellin' Band,The Flying Hat Band,Lita Ford,Gamma,Geordie,Gillan,Girl,Girlschool,Gordi,Grand Funk Railroad," +
            "Grave Digger,Gravestone,Great White,Grim Reaper,The Gun,Sammy Hagar,The Handsome Beasts,Hanoi Rocks,Hard Stuff,Hawkwind,Headpins,Heaven,Heavy Load," +
            "Heavy Metal Kids,Helix,Jimi Hendrix,High Tide,Hollow Ground,Holocaust,Holy Moses,Icon,Iron Butterfly,Iron Claw,Iron Maiden,Jag Panzer,Jaguar," +
            "Jameson Raid,Jerusalem,Josefus,JPT Scare Band,Judas Priest,Kat,Kerber,Kick Axe,Killer,Killer Dwarfs,Killing Joke,King's X,Kiss,Kix,Krokus,Leaf Hound," +
            "Leatherwolf,Led Zeppelin,Lee Aaron,Legs Diamond,Leño,Leviticus,Lionheart,Living Death,London,Lone Star,Los Suaves,Loudness,Lynyrd Skynyrd," +
            "Lucifer's Friend,Mahogany Rush,Malice,Yngwie Malmsteen,Mama's Boys,Manilla Road,Manowar,Marseille,Max Webster,May Blitz,MC5,Mentors,Mercyful Fate," +
            "Metal Church,Metallica,Ministry,Misfits,Montrose,More,Mötley Crüe,Motörhead,Mountain,Moxy,Nazareth,Necromandus,The Next Band,Nightmare,Night Sun," +
            "Nightwing,Nightwish,Ted Nugent,The Obsessed,Ozzy Osbourne,Ostrogoth,Oz,Pagan Altar,Pantera,Pentagram,Persian Risk,Picture,Pink Fairies,Praying Mantis," +
            "Pretty Maids,Primevil,Quartz,Queensrÿche,Quiet Riot,Rainbow,Rammstein,Ratt,Raven,Riot,Rock Goddess,Rok Mašina,Rose Tattoo,Uli Jon Roth,Rough Cutt," +
            "The Runaways,Running Wild,Rush,Saber Tiger,Sacred Rite,Saint,Saint Vitus,Salem,Samson,Satan,Savage,Savage Grace,Savatage,Saxon,Michael Schenker Group," +
            "Scorpions,Shark Island,Sir Lord Baltimore,Sister,Skitzo,Sorcery,Sortilège,Sound Barrier,Spinal Tap,Spider,Stampede,Starz,Steeler,Steppenwolf," +
            "The Stooges,Stormwitch,Stray,Suck,Suicidal Tendencies,Sweet Savage,Tank,Tarantula,Tesla,Thin Lizzy,Thor,TKO,Toad,Tobruk,Trance,Trespass,Triumph," +
            "Trooper,Trouble,Trust,TSA,Tucky Buzzard,Turbo,Twisted Sister,Tygers of Pan Tang,Tytan,UFO,Urchin,Uriah Heep,V8,Vandenberg,Van Halen,Vanilla Fudge," +
            "Vardis,Venom,Vicious Rumors,Virgin Steele,Vulcain,Warning,Warpig,White Sister,Whitesnake,White Spirit,White Wolf,Wild Dogs,Wild Horses," +
            "Witchfinder General,Witchfynde,Wrathchild,Wrathchild America,Y&T,Zebra,Zoetrope";
    private String[] bandListArray = bandList.split(",");
    private int bandListLength = bandListArray.length;

    public String guessTheBand() {
        String theBand;
        int pickBandNumber = ThreadLocalRandom.current().nextInt(0, bandListLength+1);
        theBand = bandListArray[pickBandNumber];
        return theBand;
    }

}
