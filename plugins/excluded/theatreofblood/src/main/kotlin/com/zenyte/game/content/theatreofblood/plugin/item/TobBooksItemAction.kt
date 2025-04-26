package com.zenyte.game.content.theatreofblood.plugin.item

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.content.Book
import com.zenyte.game.content.ChapteredBook
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.item.ItemId.*

/**
 * Handles the books that provide lore about the various bosses in Theatre of Blood.
 *
 * @author Stan van der Bend
 */

class TobBooksItemAction : ItemActionScript() {
    init {
        items(
            SERAFINAS_DIARY,
            THE_BUTCHER,
            ARACHNIDS_OF_VAMPYRIUM,
            THE_SHADOW_REALM,
            THE_WILD_HUNT,
            VERZIK_VITUR__PATIENT_RECORD
        )

        "Read" {
            val book = when(item.id) {
                SERAFINAS_DIARY -> SarafinasDiary(player)
                THE_BUTCHER -> TheButcher(player)
                ARACHNIDS_OF_VAMPYRIUM -> ArachnidsOfVampyrium(player)
                THE_SHADOW_REALM -> TheShadowRealm(player)
                THE_WILD_HUNT -> TheWildHunt(player)
                VERZIK_VITUR__PATIENT_RECORD -> VerzikViturPatientRecord(player)
                else -> error("Unknown book item $item")
            }
            Book.openBook(book)
        }
    }
}



private class SarafinasDiary(player: Player) : Book(player) {

    override fun getTitle(): String = "Sarafina's Diary"

    override fun getString(): String =
        """
            I failed him. Everything I've done, I did for him. All the pain, all the suffering. All of it. I just wanted him to live a good life, a happy life. But I failed. I was foolish. I never even considered they'd come after us at our home. I should have been smarter.
          
            It happened last week. It seemed like a simple deal at the time. Food for weapons, a fair trade. Of course, things went south. I thought I'd given them the slip, but they followed me home. That's when he got involved.
            
            He should have let me handle it. I can look after myself. Not just myself, I used to be able to look after both of us. I guess it's too late to dwell on what could have been though. The blade must have been poisoned. It barely touched him. Now, after everything, I'm losing the only family I have left. My baby brother is dying and it's all my fault.
            
            I need to get help for him. I can't let him die. No, I won't let him die. They'll be tithing our sector in a couple more weeks and he won't be well enough to cope, if he even survives for that long.
            
            I've heard of a doctor in sector five, maybe they can help. I don't have money, but I can work something out. Everyone has a price. If I've learnt nothing else from this life, I've learnt that.
            
            The doctor was a dead end, literally. Apparently the vampyres took him to Darkmeyer a few weeks ago. He'll never be seen again.
            
            Thing is, just when I thought all hope was lost, things took a strange turn. As I returned home, I was confronted by a strange looking man wearing a large medallion.
            
            The human disguise didn't fool me. He was far too clean to actually live in this place. I knew he was a vampyre. I told him he didn't scare me, but he just laughed.
            
            He came closer and called me by my name. He told me he had an offer, one I wouldn't be able to refuse. It's not the first time I've heard that. I'm no fool.
            
            Hope presents itself in mysterious ways. This strange vampyre claims to have a way to save my brother.
            
            I don't know how he knew about us. Maybe I made too much noise when asking about that doctor. It doesn't matter now.
            
            Now I have a chance to save him. I have to take it, no matter what it costs.
            
            The vampyre visited our home today. He wanted to present his offer to me. I took him down into the basement so we could talk in private.
            
            He spoke of an operation which could cure my brother of his illness. He offered no guarantees, but claimed he was confident it would work. He just needed to ensure my brother is 'compatible.'
            
            Nothing comes free around here, especially when the vampyres are involved. He had no reason to help us. He clearly wanted something in return. I asked him what it was. When he told me, I felt a shiver go down my spine.
            
            For a moment, I questioned if it was worth the cost. Was my brother worth all of that pain? I didn't debate for long. Of course he was. For him, no price is too great.
            
            Not long to go until they take my brother. Apparently, they have a secret lab where the operation will take place. The vampyre says he'll be safe there.
            
            I asked him about my own operation. How much pain would there be? Would I remember anything afterwards? He was honest with me. By the time they're done, they'll be nothing left of the person I am now. I guess that's why I've actually been using this diary for once. Soon it will be all that's left of me.
            
            Today is the day. They'll take my brother first. There's no need for him to know what I'm about to sacrifice.
            
            By the time my operation begins, he'll be fast asleep recovering from his own. I know my brother better than anyone. He'll take a long time to understand my sacrifice. Maybe he never will. I hope one day he has the heart to forgive me.
            
            I've asked the vampyre to give this diary to him. Hopefully it will help. Brother, if you are reading this, know that everything I have done has been for you. I'm sorry.
        """.trimIndent()
}

private class TheButcher(player: Player) : Book(player) {

    override fun getTitle(): String = "The Butcher"

    override fun getString(): String = """
        She came. Vampyre. Verzik. Give us what we want. Pleasure. Pain. She unDerstands. We help her. We please her. She reward us. We fail her. Punishment. Want to please. Yearn to please. Only we know hOw. She wants us to create. Create a masterpiece. We live to serve. We will build it.
        IngredieNts are simple:
        
        - Three gianT heads
        - Body of disgruntled ogre
        - Two crushed frogs
        - Four blood buckets (human)
        - Sense of pride
        - Two cyclopes arms
        - Apple pie (very important)
        - Two caTablepon legs
        
        Got ingRedients. Good. Time to bUild. Make her happy. Need reward. Need it. She can give. She can take away. She can please us. Must pleaSe her first. Getting disTracted. Time to build.
        Sew it up. Make it nice. Lots of blood, sugar and spice. Leg goes here. Arm goes there. Even got some blood to spare.
        
        BANG!
        
        Magical strawberries. Now it looks great. Make us happy. Make Verzik happy. Reward soon. Yes. Just need to sHow her.
        Go to chambErs. She'll be so pleased. Oh no! Spiders! Hate spiders! Creepy! But no Verzik? Where? Only spiders! NO! Gone. Dead. How? Why? Life is lie! Don't undeRstand. Who? Seen to much! Coming for me!
        
        Three!
        Two!
        One!
        
        BANG!
        
        Verzik pleased. We make masterpiece. Now all will see. Verzik make us happy. Pleasure. Pain. Ask us to stay. Look after it. It looks so nice. Cute. Pretty. Feeling proud.
        
        Wait what?
        
        Serafina? It was you?
        
        Bryana freesia wadset!
        Dav zed is kier!
        Curial lowell syn!
        
        Pleasure. Pain. Pleasure. Pain. Pleasure. Pain. Pleasure. Pain. Pleasure. Pain.
        
        I hate apple pie.
    """.trimIndent()
}

private class ArachnidsOfVampyrium(player: Player) : ChapteredBook(player) {

    private val introduction = "<col=000080>Introduction</col>"
    private val nylocasIschyros = "<col=000080>Nylocas Ischyros</col>"
    private val nylocasToxobolos = "<col=000080>Nylocas Toxobolos</col>"
    private val nylocasHagios = "<col=000080>Nylocas Hagios</col>"
    private val nylocasMatomenos = "<col=000080>Nylocas Matomenos</col>"
    private val nylocasAthanatos = "<col=000080>Nylocas Athanatos</col>"
    private val nylocasVasilias = "<col=000080>Nylocas Vasilias</col>"
    private val nylocasPrinkipas = "<col=000080>Nylocas Prinkipas</col>"

    override fun getTitle(): String = "Arachnids of Vampyrium"

    override fun getChapters(): Array<String> =
        arrayOf(
            introduction,
            nylocasIschyros,
            nylocasToxobolos,
            nylocasHagios,
            nylocasMatomenos,
            nylocasAthanatos,
            nylocasVasilias,
            nylocasPrinkipas
        )

    override fun getContent(): Array<String> =
        arrayOf(
            """
                $introduction<br>
                The nylocas are a diverse group of arachnids native to the world of Vampyrium. On their own, they are easily manageable and possess little threat to the vampyre population. However, nylocas typically group together in clusters. As such, they have the potential to quickly overcome more powerful foes.
                When the Stranger from Afar led the vampyres to Gielinor, a large number of nylocas travelled with them. These nylocas were in the possession of Verzik Vitur, who had domesticated several of their species.
                Over her millennia spent on Gielinor, Verzik has created a diverse breeding progream for the nylocas. Today, the number on Gielinor far exceeds that of their original Vampyrium ancestors.
                Although there are only three true species of nylocas, the generations of selective breeding have resulted in four new variants being identified on Gielinor. The purpose of this book is to detail the different types of nylocas.
            """.trimIndent(),
            """
                $nylocasIschyros<br>
                One of the three true nylocas originally from Vampyrium. The Ischyros are grey in colour and are notable for their hard, spine covered abdomens. The size of these nylocas can vary, but they typically do not grow much bigger than an adult human. The legs of an Ischyros end in sharp claws, which they aptly use against their foes.
            """.trimIndent(),
            """
                $nylocasToxobolos<br>
                Another of the three nylocas native to Vampyrium. The Toxobolos can easily be distinguished by their dark green colour and their wide, leaf-shaped claws. Like the Ischyros, they vary in size but generally do not grow larger than an adult human. Whilst they do use their claws offensively in the same way the Ischyros do, their preferred method of combat is to launch deadly spines at prey and predator alike.
            """.trimIndent(),
            """
                $nylocasHagios<br>
                The last of the true nylocas. The Hagios are notable for their light blue colour and for the magic that visibly radiates from their exoskeleton. In size and general behaviour, they are similar to the other true nylocas. However, the Hagios also have the ability to target their foes with dangerous magical attacks.
            """.trimIndent(),
            """
                $nylocasMatomenos<br>
                The Matomenos are one of the new variant subspecies of nylocas that were bred by Verzik Vitur. They are slightly larger than true nylocas and are easy to identify due to their blood red colour. Likely due to their domestication, the Matomenos are not as aggressive as other nylocas and rarely attack, even if provoked. Instead, they are purely scavengers that will feast on the blood of already dead prey. This blood is stored in their abdomen, adding to their distinct colour.
            """.trimIndent(),
            """
                $nylocasAthanatos<br>
                The Athanatos are the second subspecies of nylocas that were bred as part of Verzik Vitur's breeding program. Created through selective inbreeding of Matomenos, the two species are very closely related. The Athanatos are slightly larger then the Matomenos and like them, they will drink the blood of fallen foes and store it in their abdomen. However, the Athanatos have the ability to modify this blood internally, giving it unique properties. This modified blood gives them their distinctive purple colour. Although Athanatos are not directly aggressive, they will still support their kin by launching blood with healing properties.
            """.trimIndent(),
            """
                $nylocasVasilias<br>
                Another new variant of the nylocas and arguably the most dangerous. The Vasilias are much larger then their cousins and have a limited ability to shapeshift, taking on the appearance and abilities of true nylocas. As the newest variant of nylocas, they are still a rarity compared to other types. However, thanks to their innate abilities, one Vasilias has the potential to do more damage than an entire cluster of other variants.
            """.trimIndent(),
            """
                $nylocasPrinkipas<br>
                The Prinkipas are the last of the four new variants found on Gielinor. While considered a unique type of nylocas, the reality is that the Prinkipas are very closely related to the Vasilias. Like the Vasilias, they have the ability to shapeshift into forms that resemble true nylocas. However, the Prinkipas, while stronger than most nylocas, are still notably weaker than the Vasilias. As such, the Prinkipas were likely a precursor to the breeding of the more successful Vasilias variant.
            """.trimIndent()
        )
}

private class TheShadowRealm(player: Player) : ChapteredBook(player) {

    private val experiment1 = "<col=000080>Experiment 1</col>"
    private val experiment2 = "<col=000080>Experiment 2</col>"
    private val experiment3 = "<col=000080>Experiment 3</col>"

    override fun getTitle(): String = "The shadow realm"

    override fun getChapters(): Array<String> =
        arrayOf(
            "",
            experiment1,
            experiment2,
            experiment3,
        )

    override fun getContent(): Array<String> =
        arrayOf(
            """
                Lord Drakan has tasked me with learning more about the shadow realm. To aid in this task, I've been given a dark beast. The natural connection this creature has with the shadows should make the task far easier.<br>
            """.trimIndent(),
            """
                $experiment1<br>
                Success! The creature has managed to connect to the other side, and early telepathic intrusions show that they've made contact. It's too early to say who wishes to communicate with us, but the connection will be stronger next time. I'll just need to make some alterations to the creature.
                'Closer!' - Or at least, that is what it sounds like...<br>
            """.trimIndent(),
            """
                $experiment2<br>
                Incredible! My alterations were just what was needed to give the creature more power. It can now move freely beyond realms and it has returned with yet another message.
                'Abandon deeper, the tortured experiment won! Trapped! Follow within the shadows voice.'
                Peculiar non-sense, but I assume there must've been some kind of interference. Perhaps the owner of this voice has been trapped on the other side? I'll send the creature back, but with a message!
                'Who are you?'<br>
            """.trimIndent(),
            """
                $experiment3<br>
                Perfect! The creature has managed to gather an answer from the other side, but again the answer has been mangled into obscurity. I believe everything is in place now for the final practical test! I will travel with the creature and ask this mysterious being about the secrets of the shadow realm. Lord Drakan will be most impressed with my findings. I can already taste the blood prize and it is delicious.
                'So!'
                'Tet!'
                'Seg!'
                
                'So!'
                'Tet!'
                
                'Seg!'
            """.trimIndent()
        )
}

private class TheWildHunt(player: Player) : Book(player) {
    override fun getTitle(): String = "The Wild Hunt"

    override fun getString(): String = """
        There is beauty in chaos. Of this, there is no greater example than Vampyrium, my home. Ever since I left, I have dreamt of my eventual return. Now, millennia later, I am finally here. The world is just how I remembered it. With its blood-red sky and forests of darkwood, it is a beauty unlike any other.<br>
        I did not just come here to admire though, for today marks the first step in what will eventually make things right. For too long, my people have been softened by the Stranger from Afar and his false ideals. They are no longer hunters, but instead, mere farmers. Soon however, I shall teach them to embrace our heritage, and remind them what it means to be a true vampyre.<br>
        First though, I have some unfinished business to attend to. There is an old friend who I have left waiting for far too long. Before the Stranger from Afar domesticated us, our people were hunters unlike any others. No creature on Vampyrium could match our strength or our cunning. No creature, except one. The yarasa.<br>
        The yarasa were our true equals. Strong, fast, and unpredictable. They made for the ultimate hunt. Alas, the yarasa were amongst the many casualties of our domestication. Before then, we were a divided people. But with unity under the eight tribes came a power we had never before experienced. We massacred the yarasa with ease, their strength no match for our superior numbers.<br>
        I have many regrets, but none more so than the events of that day. It was no hunt, just a joyless slaughter. It was our first step towards what we have become now, and I am filled with self loathing for my role in it. However, today is not about that. Today is about one thing, and one thing alone. Today, for the first time in millennia, the hunt begins again.<br>
        While most of the yarasa fell that day, there were some who managed to evade our slaughter. Most of these were drawn out and killed soon afterwards. However, there was one who managed to defy us right up to the day we left Vampyrium. Xarpus, the last king of the yarasa. The yarasa were a formidable foe, but compared to Xarpus, they were no foe at all. He is the apex predator of Vampyrium and he is my target.<br>
        I had no doubt that he would have survived all these years. With the rest of his kind dead and most of the vampyres having left the world, he has had little competition. Still, I don't expect he will have grown weak. Like me, he has waited a long time for this day. At long last, I will face a true foe again. At long last, I will feel the joy of the hunt again.<br>
        I have not allowed myself to fall victim to the sloth and gluttony that has taken hold of the rest of my people. I should still have the strength to take on this challenge. However, if I should fall, let it be known that I am finally free again. I spent too long a slave to the Stranger from Afar, too long a slave to myself. No more. Let the hunt begin.
    """.trimIndent()
}

private class VerzikViturPatientRecord(player: Player) : ChapteredBook(player) {

    private val session1 = "<col=000080>Session 1</col>"
    private val session2 = "<col=000080>Session 2</col>"
    private val session3 = "<col=000080>Session 3</col>"
    private val session4 = "<col=000080>Session 4</col>"
    private val session5 = "<col=000080>Session 5</col>"

    override fun getTitle(): String = "Verzik Vitur - patient record"

    override fun getChapters(): Array<String> =
        arrayOf(
            session1,
            session2,
            session3,
            session4,
            session5,
        )

    override fun getContent(): Array<String> =
        arrayOf(
            """
                $session1<br>
                I have been asked by Verzik herself to look into a minor illness. She believes was caused by the bite of a nylocas. Verzik has requested absolute privacy in this matter. As such, I am seeing her in her personal chambers in Ver Sinhaza.
                Verzik claims to have been suffering from a fever for the past few days. She says that this started not long after she was bitten by one of her nylocas. The nylocas in question was a new variant, the result of her extensive breeding program. However, the nylocas died not long after biting Verzik and as such cannot be examined.
                After a quick inspection, I can confirm that Verzik is suffering from a slight fever, something that isn't uncommon in vampyres. I have prescribed a guam potion that should help it clear up.<br>
            """.trimIndent(),
            """
                $session2<br>
                I have been asked to return to Ver Sinhaza to see Verzik again. Although she has been taking the guam potion for a week, her condition has actually deteriorated rather than improved.
                An obvious change during this visit is the apparent weight gained since I last saw her. She claims this is due to the illness preventing her from exercising. However, I am concerned over how rapid the increase has been.
                I have decided to run a few more tests on Verzik to try and determine the cause of her illness. I will have the results of these in a few days. Once I have them, we can determine a suitable course of action.
            """.trimIndent(),
            """
                $session3<br>
                I have the results of Verzik's tests and so I have returned to Ver Sinhaza again. I had to wait a short while as Verzik was having a meeting with Ranis Drakan. This meant we didn't have as much time for our appointment as usual.
                Verzik's condition has not improved since we last met. Her weight increase does not appear to have slowed. However, what is more concerning is the results of her tests. The tests show a large number of what appear to be tiny eggs within her body, possibly from the nylocas that bit her.
                The presence of the eggs seems to be having a negative impact on her vampyric abilities, including her immunities. Her ability to shapeshift however, seems unaffected.
                I have asked Verzik to spend a few days at my surgery in Darkmeyer so that I can properly monitor her condition and perform an operation to remove the eggs. However, she has refused, not wishing to leave Ver Sinhaza. As such, I will need to perform the operation here instead. This will take place tomorrow before her condition further deteriorates.<br>
            """.trimIndent(),
            """
                $session4<br>
                Today I performed the operation on Verzik at Ver Sinhaza. I was once again delayed by another of Verzik's meetings, this time with that crazed vampyre they call the butcher.
                The operation went surprisingly smoothly and all of the eggs appear to have been removed. Verzik has requested that I let her keep some of the eggs for experimentation. I agreed. After all, she's not the kind of person you deny. I also took some eggs myself so I could inspect them further.
                I have arranged to return in a few days time to see how things are doing, but I hope that the symptoms will now subside.<br>
            """.trimIndent(),
            """
                $session5<br>
                Something's gone wrong, very wrong. I've returned to check up on Verzik and found something horrifying. There was something else inside her, and the eggs have returned. I tried to remove them but Verzik stopped me. It's clear her mind is no longer her own. She has insisted I leave.
                I tried telling her she needs help, that I need to take her to my surgery. She has refused. I can't change her mind and I have no power to force her. Instead, all I can do is finish these notes and leave. Verzik has been giving me a strange look ever since I arrived and I no longer feel safe here.<br>
            """.trimIndent()
        )
}
