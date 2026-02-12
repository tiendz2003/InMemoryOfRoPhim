package com.manutd.rophim.core.data.utils

import com.manutd.ronaldo.network.model.Actor
import com.manutd.ronaldo.network.model.Channel
import com.manutd.ronaldo.network.model.ChannelType
import com.manutd.ronaldo.network.model.Gender
import com.manutd.ronaldo.network.model.Group
import com.manutd.ronaldo.network.model.HomeData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

//fake data dựa theo response của rổ phim
object FakeDataProvider {

    fun getHomeData(): HomeData {
        return HomeData(
            color = "#38003C",
            description = "Ứng dụng xem phim trực tuyến hàng đầu",
            gridNumber = 2,
            groups = persistentListOf(
                getCarouselGroup(),
                getHorizontalGroup("Phim Hành Động"),
                getTopRankedGroup(),
                getHorizontalGroup("Phim Hoạt Hình"),
                getHorizontalGroup("Phim Kinh Dị"),
                getHorizontalGroup("Phim Hài"),
                getActorGroup()
            ),
            id = "home_001",
            image = "https://image.tmdb.org/t/p/original/backdrop.jpg",
            name = "Trang Chủ"
        )
    }

    private fun getActorGroup(): Group {
        return Group(
            id = "actors_popular",
            display = "horizontal",
            name = "Diễn Viên Nổi Tiếng",
            type = ChannelType.ACTOR,
            remoteUrl = null,
            channels = persistentListOf(),
            actors = getPopularActors()
        )
    }

    private fun getPopularActors(): ImmutableList<Actor> {
        return persistentListOf(
            // Vietnamese Actors
            Actor(
                id = "actor_001",
                name = "Leonardo Dicaprio",
                photoUrl = "https://numpaint.com/wp-content/uploads/2020/08/young-leonardo-dicaprio-paint-by-numbers.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_002",
                name = "Brad Pitt",
                photoUrl = "https://hips.hearstapps.com/hmg-prod/images/actor-brad-pitt-arrives-for-the-premiere-of-the-mexican-news-photo-1591819497.jpg?crop=1.00xw:0.723xh;0,0.0233xh",
                gender = Gender.MALE,

                ),

            // Hollywood Stars
            Actor(
                id = "actor_003",
                name = "Tom Cruise",
                photoUrl = "https://image.tmdb.org/t/p/original/eOh4ubpOm2Igdg0QH2ghj0mFtC.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_004",
                name = "Margot Robbie",
                photoUrl = "https://image.tmdb.org/t/p/original/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
                gender = Gender.FEMALE,

                ),
            Actor(
                id = "actor_005",
                name = "Ryan Gosling",
                photoUrl = "https://image.tmdb.org/t/p/original/lyUyVARQKhGxaxy0FbPJCQRpiaW.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_006",
                name = "Zendaya",
                photoUrl = "https://image.tmdb.org/t/p/original/kg9ICat0odCQLnz5Qw8jOsz8q5y.jpg",
                gender = Gender.FEMALE,

                ),
            Actor(
                id = "actor_007",
                name = "Tom Holland",
                photoUrl = "https://image.tmdb.org/t/p/original/2qhIDp44cAqP2clOgt2afQI07X8.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_008",
                name = "Scarlett Johansson",
                photoUrl = "https://image.tmdb.org/t/p/original/3JTEc2tGUact9c0WktvpeJ9pajn.jpg",
                gender = Gender.FEMALE,

                ),
            Actor(
                id = "actor_009",
                name = "Chris Hemsworth",
                photoUrl = "https://image.tmdb.org/t/p/original/jpurJ9jAcLCYjgHHfYF32m3zJYm.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_010",
                name = "Gal Gadot",
                photoUrl = "https://image.tmdb.org/t/p/original/fysvehTvU6bE3JgxaOTRfvQJzJ4.jpg",
                gender = Gender.FEMALE,

                ),
            Actor(
                id = "actor_011",
                name = "Jason Momoa",
                photoUrl = "https://image.tmdb.org/t/p/original/6dEFBpZH8C8OijsynkSajQT99Pb.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_012",
                name = "Florence Pugh",
                photoUrl = "https://image.tmdb.org/t/p/original/6Sjz9teWjrMY9lF2o9FCo4XmoRh.jpg",
                gender = Gender.FEMALE,

                ),
            Actor(
                id = "actor_013",
                name = "Timothée Chalamet",
                photoUrl = "https://image.tmdb.org/t/p/original/9j9X0fhfV9gPSOMCfCRmZKBx4pN.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_014",
                name = "Ana de Armas",
                photoUrl = "https://image.tmdb.org/t/p/original/3vxvsmYLTf4jnr163SUlBIw51ee.jpg",
                gender = Gender.FEMALE,

                ),
            Actor(
                id = "actor_015",
                name = "Pedro Pascal",
                photoUrl = "https://image.tmdb.org/t/p/original/9VYK7oxcqhjd5LAH6ZFJ3XzOlID.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_016",
                name = "Anya Taylor-Joy",
                photoUrl = "https://image.tmdb.org/t/p/original/yZpghhtKM2VZHDx6JGAZqAVU4qG.jpg",
                gender = Gender.FEMALE,

                ),
            Actor(
                id = "actor_017",
                name = "Keanu Reeves",
                photoUrl = "https://image.tmdb.org/t/p/original/rRdru6REr9i3WsMsNdwuGphjC3s.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_018",
                name = "Emma Stone",
                photoUrl = "https://image.tmdb.org/t/p/original/wqEypkRUUZEcFmPV4O4JpZznmNk.jpg",
                gender = Gender.FEMALE,

                ),
            Actor(
                id = "actor_019",
                name = "Dwayne Johnson",
                photoUrl = "https://image.tmdb.org/t/p/original/5QApZVV8FUFlVxQpIK3Ew6cqotq.jpg",
                gender = Gender.MALE,

                ),
            Actor(
                id = "actor_020",
                name = "Jennifer Lawrence",
                photoUrl = "https://image.tmdb.org/t/p/original/r6iLqsVxDMhOPCL0yRQlLGkap2f.jpg",
                gender = Gender.FEMALE,

                )
        )
    }

    private fun getCarouselGroup(): Group {
        return Group(
            id = "carousel_001",
            display = "slider",
            name = "Trending Now",
            type = ChannelType.SLIDER,
            remoteUrl = null,
            channels = persistentListOf(
                Channel(
                    id = "movie_001",
                    name = "Titanic",
                    display = "Titanic",
                    description = "Phim Titanic kể về câu chuyện tình yêu đầy bi kịch giữa cô quý tộc mười bảy tuổi Rose và nghệ sĩ nghèo Jack trên con tàu huyền thoại R.M.S Titanic",
                    logoUrl = "https://m.media-amazon.com/images/I/71i6L1vZjiL._AC_SY879_.jpg",
                    streamUrl = "https://example.com/stream/last_kingdom.m3u8",
                    shareUrl = "https://example.com/share/last_kingdom",
                    imdb = "8.5",
                    quality = "4K",
                    rating = "4.5",
                    year = "1997",
                    episode = "Film"
                ),
                Channel(
                    id = "movie_002",
                    name = "Extraction 2",
                    display = "Extraction 2: Nhiệm Vụ Giải Cứu",
                    description = "Tyler Rake trở lại với nhiệm vụ nguy hiểm mới - giải cứu gia đình của một tên tội phạm tàn bạo khỏi nhà tù Georgia.",
                    logoUrl = "https://image.tmdb.org/t/p/original/7gKI9hpEMcZUQpNgKrkDzJpbnNS.jpg",
                    streamUrl = "https://example.com/stream/extraction2.m3u8",
                    shareUrl = "https://example.com/share/extraction2",
                    imdb = "7.8",
                    quality = "4K",
                    rating = "4.2",
                    year = "2023",
                    episode = "Film"
                ),
                Channel(
                    id = "movie_003",
                    name = "Rebel Moon",
                    display = "Rebel Moon: Phần 1 - Người Con Gái Của Lửa",
                    description = "Khi một quân đội tàn bạo tấn công một thuộc địa hòa bình ở rìa thiên hà, những người sống sót tìm kiếm các chiến binh lạ để giúp họ chiến đấu.",
                    logoUrl = "https://image.tmdb.org/t/p/original/ui4DrH1cKk2vkHshcUcGt2lKxCm.jpg",
                    streamUrl = "https://example.com/stream/rebel_moon.m3u8",
                    shareUrl = "https://example.com/share/rebel_moon",
                    imdb = "7.2",
                    quality = "4K",
                    rating = "3.8",
                    year = "2023",
                    episode = "Film"
                ),
                Channel(
                    id = "movie_004",
                    name = "The Mother",
                    display = "The Mother: Bản Năng Mẹ",
                    description = "Một sát thủ chết người ra khỏi nơi ẩn náu để bảo vệ cô con gái mà cô đã từ bỏ nhiều năm trước, trong khi đang chạy trốn những người đàn ông nguy hiểm.",
                    logoUrl = "https://image.tmdb.org/t/p/original/vnRthEZz16Q9VWcP5homkHxyHoy.jpg",
                    streamUrl = "https://example.com/stream/the_mother.m3u8",
                    shareUrl = "https://example.com/share/the_mother",
                    imdb = "7.0",
                    quality = "HD",
                    rating = "3.9",
                    year = "2023",
                    episode = "Film"
                ),
                Channel(
                    id = "movie_005",
                    name = "Murder Mystery 2",
                    display = "Murder Mystery 2: Bí Ẩn Giết Người 2",
                    description = "Nick và Audrey Spitz tham gia một sự kiện sang trọng trên hòn đảo riêng tư của một tỷ phú. Nhưng khi bắt cóc xảy ra, họ lại bị cuốn vào một vụ án mạng mới.",
                    logoUrl = "https://image.tmdb.org/t/p/original/s1VzVhXlqsevi8zeCMG9A16nEUf.jpg",
                    streamUrl = "https://example.com/stream/murder_mystery2.m3u8",
                    shareUrl = "https://example.com/share/murder_mystery2",
                    imdb = "6.8",
                    quality = "HD",
                    rating = "3.5",
                    year = "2023",
                    episode = "Film"
                )
            )
        )
    }

    private fun getHorizontalGroup(title: String): Group {
        val movieData = when (title) {
            "Phim Hành Động" -> getActionMovies()
            "Phim Hoạt Hình" -> getAnimationMovies()
            "Phim Kinh Dị" -> getHorrorMovies()
            "Phim Hài" -> getComedyMovies()
            else -> getActionMovies()
        }

        return Group(
            id = "horizontal_${title.hashCode()}",
            display = "horizontal",
            name = title,
            type = ChannelType.HORIZONTAL,
            remoteUrl = null,
            channels = movieData.toImmutableList()
        )
    }

    private fun getTopRankedGroup(): Group {
        return Group(
            id = "top_001",
            display = "horizontal",
            name = "Top 10 Phim Được Xem Nhiều Nhất",
            type = ChannelType.TOP,
            remoteUrl = null,
            channels = persistentListOf(
                Channel(
                    id = "top_001",
                    name = "Breaking Bad",
                    display = "Breaking Bad: Trở Thành Tội Phạm",
                    description = "Một giáo viên hóa học được chẩn đoán mắc bệnh ung thư phổi hợp tác với một cựu học sinh để sản xuất và bán methamphetamine.",
                    logoUrl = "https://image.tmdb.org/t/p/original/ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg",
                    streamUrl = "https://example.com/stream/breaking_bad.m3u8",
                    shareUrl = "https://example.com/share/breaking_bad",
                    imdb = "9.5",
                    quality = "4K",
                    rating = "5.0",
                    year = "2008-2013",
                    episode = "62 tập"
                ),
                Channel(
                    id = "top_002",
                    name = "Stranger Things",
                    display = "Stranger Things: Những Điều Kỳ Bí",
                    description = "Khi một cậu bé trẻ biến mất, thị trấn nhỏ của anh phơi bày một bí ẩn liên quan đến các thí nghiệm bí mật, lực lượng siêu nhiên đáng sợ và một cô gái kỳ lạ.",
                    logoUrl = "https://image.tmdb.org/t/p/original/49WJfeN0moxb9IPfGn8AIqMGskD.jpg",
                    streamUrl = "https://example.com/stream/stranger_things.m3u8",
                    shareUrl = "https://example.com/share/stranger_things",
                    imdb = "8.7",
                    quality = "4K",
                    rating = "4.7",
                    year = "2016-2024",
                    episode = "42 tập"
                ),
                Channel(
                    id = "top_003",
                    name = "The Witcher",
                    display = "The Witcher: Thợ Săn Quái Vật",
                    description = "Geralt of Rivia, một thợ săn quái vật đột biến, đấu tranh để tìm vị trí của mình trong một thế giới nơi con người thường chứng tỏ độc ác hơn quái vật.",
                    logoUrl = "https://image.tmdb.org/t/p/original/7vjaCdMw15FEbXyLQTVa04URsPm.jpg",
                    streamUrl = "https://example.com/stream/the_witcher.m3u8",
                    shareUrl = "https://example.com/share/the_witcher",
                    imdb = "8.2",
                    quality = "4K",
                    rating = "4.3",
                    year = "2019-2023",
                    episode = "24 tập"
                ),
                Channel(
                    id = "top_004",
                    name = "Wednesday",
                    display = "Wednesday: Thứ Tư",
                    description = "Wednesday Addams học cách làm chủ khả năng ngoại cảm của mình, ngăn chặn một cơn giết chóc tàn bạo và giải quyết bí ẩn siêu nhiên 25 năm trước liên quan đến cha mẹ cô.",
                    logoUrl = "https://image.tmdb.org/t/p/original/9PFonBhy4cQy7Jz20NpMygczOkv.jpg",
                    streamUrl = "https://example.com/stream/wednesday.m3u8",
                    shareUrl = "https://example.com/share/wednesday",
                    imdb = "8.1",
                    quality = "4K",
                    rating = "4.4",
                    year = "2022",
                    episode = "8 tập"
                ),
                Channel(
                    id = "top_005",
                    name = "The Crown",
                    display = "The Crown: Vương Miện",
                    description = "Một cái nhìn sâu sắc vào cuộc đời của Nữ hoàng Elizabeth II và các sự kiện chính trị lịch sử đã định hình nước Anh trong nửa sau của thế kỷ 20.",
                    logoUrl = "https://image.tmdb.org/t/p/original/1M876KPjulVwppEpldhdc8V4o68.jpg",
                    streamUrl = "https://example.com/stream/the_crown.m3u8",
                    shareUrl = "https://example.com/share/the_crown",
                    imdb = "8.6",
                    quality = "4K",
                    rating = "4.5",
                    year = "2016-2023",
                    episode = "60 tập"
                ),
                Channel(
                    id = "top_006",
                    name = "Money Heist",
                    display = "Money Heist: Vụ Cướp Hoàn Hảo",
                    description = "Một băng nhóm tội phạm được lên kế hoạch cẩn thận thực hiện vụ cướp lớn nhất trong lịch sử Tây Ban Nha - in 2,4 tỷ euro tại Nhà in Tiền Hoàng gia.",
                    logoUrl = "https://image.tmdb.org/t/p/original/MoEKaPFHABtA1xKoOteirGaHl1.jpg",
                    streamUrl = "https://example.com/stream/money_heist.m3u8",
                    shareUrl = "https://example.com/share/money_heist",
                    imdb = "8.2",
                    quality = "4K",
                    rating = "4.4",
                    year = "2017-2021",
                    episode = "41 tập"
                ),
                Channel(
                    id = "top_007",
                    name = "The Last of Us",
                    display = "The Last of Us: Tận Thế",
                    description = "20 năm sau khi nền văn minh hiện đại bị phá hủy bởi đại dịch, Joel được thuê để đưa Ellie 14 tuổi ra khỏi vùng kiểm dịch áp bức.",
                    logoUrl = "https://image.tmdb.org/t/p/original/uKvVjHNqB5VmOrdxqAt2F7J78ED.jpg",
                    streamUrl = "https://example.com/stream/the_last_of_us.m3u8",
                    shareUrl = "https://example.com/share/the_last_of_us",
                    imdb = "8.8",
                    quality = "4K",
                    rating = "4.6",
                    year = "2023",
                    episode = "9 tập"
                ),
                Channel(
                    id = "top_008",
                    name = "Squid Game",
                    display = "Squid Game: Trò Chơi Con Mực",
                    description = "Hàng trăm người chơi mắc nợ chấp nhận một lời mời kỳ lạ để thi đấu trong các trò chơi trẻ em. Chờ đợi họ là giải thưởng lớn - và những rủi ro chết người.",
                    logoUrl = "https://image.tmdb.org/t/p/original/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg",
                    streamUrl = "https://example.com/stream/squid_game.m3u8",
                    shareUrl = "https://example.com/share/squid_game",
                    imdb = "8.0",
                    quality = "4K",
                    rating = "4.3",
                    year = "2021",
                    episode = "9 tập"
                ),
                Channel(
                    id = "top_009",
                    name = "Peaky Blinders",
                    display = "Peaky Blinders: Giang Hồ Anh Quốc",
                    description = "Một gia đình băng đảng có trụ sở tại Birmingham, Anh vào năm 1919, với một ông trùm hung bạo đứng đầu một băng nhóm tội phạm khét tiếng.",
                    logoUrl = "https://image.tmdb.org/t/p/original/vUUqzWa2LnHIVqkaKVlVGkVcZIW.jpg",
                    streamUrl = "https://example.com/stream/peaky_blinders.m3u8",
                    shareUrl = "https://example.com/share/peaky_blinders",
                    imdb = "8.8",
                    quality = "4K",
                    rating = "4.7",
                    year = "2013-2022",
                    episode = "36 tập"
                ),
                Channel(
                    id = "top_010",
                    name = "Dark",
                    display = "Dark: Bóng Tối",
                    description = "Một cuộc tìm kiếm hai trẻ em mất tích vạch trần những bí mật của bốn gia đình khi họ làm sáng tỏ một âm mưu du hành thời gian kéo dài ba thế hệ.",
                    logoUrl = "https://image.tmdb.org/t/p/original/5tRS9ET1VzIbnduJPfmYxOn746R.jpg",
                    streamUrl = "https://example.com/stream/dark.m3u8",
                    shareUrl = "https://example.com/share/dark",
                    imdb = "8.8",
                    quality = "4K",
                    rating = "4.7",
                    year = "2017-2020",
                    episode = "26 tập"
                )
            )
        )
    }

    private fun getActionMovies(): List<Channel> {
        return listOf(
            Channel(
                id = "action_001",
                name = "John Wick 4",
                display = "John Wick: Chapter 4",
                description = "John Wick khám phá một con đường để đánh bại High Table. Nhưng trước khi anh có thể giành được tự do, anh phải đối mặt với một kẻ thù mới.",
                logoUrl = "https://image.tmdb.org/t/p/original/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg",
                streamUrl = "https://example.com/stream/john_wick_4.m3u8",
                shareUrl = "https://example.com/share/john_wick_4",
                imdb = "8.3",
                quality = "4K",
                rating = "4.5",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "action_002",
                name = "Fast X",
                display = "Fast X: Quá Nhanh Quá Nguy Hiểm 10",
                description = "Trong một nhiệm vụ kéo dài hơn một thập kỷ, Dominic Toretto và gia đình anh phải đối mặt với kẻ thù nguy hiểm nhất từ trước đến nay.",
                logoUrl = "https://image.tmdb.org/t/p/original/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",
                streamUrl = "https://example.com/stream/fast_x.m3u8",
                shareUrl = "https://example.com/share/fast_x",
                imdb = "7.5",
                quality = "4K",
                rating = "4.0",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "action_003",
                name = "Mission Impossible 7",
                display = "Mission: Impossible - Dead Reckoning Part One",
                description = "Ethan Hunt và nhóm IMF phải theo dõi một vũ khí đáng sợ mới đe dọa toàn nhân loại trước khi nó rơi vào tay kẻ xấu.",
                logoUrl = "https://image.tmdb.org/t/p/original/NNxYkU70HPurnNCSiCjYAmacwm.jpg",
                streamUrl = "https://example.com/stream/mission_impossible_7.m3u8",
                shareUrl = "https://example.com/share/mission_impossible_7",
                imdb = "8.1",
                quality = "4K",
                rating = "4.4",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "action_004",
                name = "The Equalizer 3",
                display = "The Equalizer 3: Người Cân Bằng 3",
                description = "Robert McCall tìm thấy một ngôi nhà mới ở miền Nam nước Ý nhưng phát hiện bạn bè mới của mình đang bị kiểm soát bởi những ông trùm tội phạm địa phương.",
                logoUrl = "https://image.tmdb.org/t/p/original/tvX2JltXjmpHGQ49LDYXMfbJIYb.jpg",
                streamUrl = "https://example.com/stream/equalizer_3.m3u8",
                shareUrl = "https://example.com/share/equalizer_3",
                imdb = "7.8",
                quality = "4K",
                rating = "4.2",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "action_005",
                name = "The Creator",
                display = "The Creator: Đấng Sáng Tạo",
                description = "Giữa một cuộc chiến trong tương lai giữa loài người và lực lượng trí tuệ nhân tạo, Joshua, một cựu đặc nhiệm được giao nhiệm vụ săn lùng Creator.",
                logoUrl = "https://image.tmdb.org/t/p/original/vBZ0qvaRxqEhZwl6LWmruJqWE8Z.jpg",
                streamUrl = "https://example.com/stream/the_creator.m3u8",
                shareUrl = "https://example.com/share/the_creator",
                imdb = "7.6",
                quality = "4K",
                rating = "4.1",
                year = "2023",
                episode = "Film"
            )
        )
    }

    private fun getAnimationMovies(): List<Channel> {
        return listOf(
            // Studio Ghibli Films
            Channel(
                id = "animation_001",
                name = "Spirited Away",
                display = "Spirited Away: Vùng Đất Linh Hồn",
                description = "Chihiro, một cô bé 10 tuổi, bước vào thế giới kỳ ảo của các vị thần và phù thủy khi cha mẹ cô bị biến thành lợn. Cô phải làm việc trong nhà t목욕 của phù thủy Yubaba để giải cứu gia đình.",
                logoUrl = "https://image.tmdb.org/t/p/original/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg",
                streamUrl = "https://example.com/stream/spirited_away.m3u8",
                shareUrl = "https://example.com/share/spirited_away",
                imdb = "8.6",
                quality = "HD",
                rating = "4.8",
                year = "2001",
                episode = "Film"
            ),
            Channel(
                id = "animation_002",
                name = "My Neighbor Totoro",
                display = "My Neighbor Totoro: Hàng Xóm Của Tôi Là Totoro",
                description = "Hai chị em Satsuki và Mei chuyển đến vùng nông thôn và phát hiện ra thế giới kỳ diệu của các sinh vật rừng, đặc biệt là Totoro, một linh hồn rừng khổng lồ và thân thiện.",
                logoUrl = "https://image.tmdb.org/t/p/original/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg",
                streamUrl = "https://example.com/stream/totoro.m3u8",
                shareUrl = "https://example.com/share/totoro",
                imdb = "8.2",
                quality = "HD",
                rating = "4.7",
                year = "1988",
                episode = "Film"
            ),
            Channel(
                id = "animation_003",
                name = "Howl's Moving Castle",
                display = "Howl's Moving Castle: Lâu Đài Di Động Của Howl",
                description = "Sophie bị biến thành một bà lão 90 tuổi bởi lời nguyền của Phù thủy Hoang Mạc. Cô tìm đến lâu đài di động của phù thủy Howl để tìm cách phá lời nguyền.",
                logoUrl = "https://m.media-amazon.com/images/I/81M0Eewr7QL._AC_UF894,1000_QL80_.jpg",
                streamUrl = "https://example.com/stream/howls_castle.m3u8",
                shareUrl = "https://example.com/share/howls_castle",
                imdb = "8.2",
                quality = "HD",
                rating = "4.7",
                year = "2004",
                episode = "Film"
            ),
            Channel(
                id = "animation_004",
                name = "Princess Mononoke",
                display = "Princess Mononoke: Công Chúa Mononoke",
                description = "Ashitaka, một hoàng tử trẻ, bị nguyền rủa và phải tìm cách chữa trị. Anh gặp San, một cô gái sống cùng sói, trong cuộc chiến giữa con người và thần linh rừng.",
                logoUrl = "https://image.tmdb.org/t/p/original/cMYCDADoLKLbB83g4WnJegaZimC.jpg",
                streamUrl = "https://example.com/stream/mononoke.m3u8",
                shareUrl = "https://example.com/share/mononoke",
                imdb = "8.4",
                quality = "HD",
                rating = "4.7",
                year = "1997",
                episode = "Film"
            ),
            Channel(
                id = "animation_005",
                name = "Ponyo",
                display = "Ponyo: Cô Bé Cá Vàng Trên Vách Đá",
                description = "Ponyo, một cá vàng kỳ diệu, muốn trở thành người sau khi kết bạn với Sosuke, một cậu bé 5 tuổi. Tình bạn của họ đe dọa cân bằng tự nhiên của thế giới.",
                logoUrl = "https://m.media-amazon.com/images/M/MV5BZDkzMzQ5ZmQtOTA3MC00MjhiLTk5M2UtNzk0MjEzZmVjN2UxXkEyXkFqcGc@._V1_.jpg",
                streamUrl = "https://example.com/stream/ponyo.m3u8",
                shareUrl = "https://example.com/share/ponyo",
                imdb = "7.7",
                quality = "HD",
                rating = "4.3",
                year = "2008",
                episode = "Film"
            ),

            // Disney Pixar Films
            Channel(
                id = "animation_006",
                name = "Encanto",
                display = "Encanto: Vùng Đất Phép Thuật",
                description = "Mirabel là thành viên duy nhất trong gia đình Madrigal không có phép thuật. Khi ngôi nhà kỳ diệu của họ gặp nguy hiểm, cô phát hiện rằng mình có thể là hy vọng cuối cùng.",
                logoUrl = "https://image.tmdb.org/t/p/original/4j0PNHkMr5ax3IA8tjtxcmPU3QT.jpg",
                streamUrl = "https://example.com/stream/encanto.m3u8",
                shareUrl = "https://example.com/share/encanto",
                imdb = "7.2",
                quality = "4K",
                rating = "4.5",
                year = "2021",
                episode = "Film"
            ),
            Channel(
                id = "animation_007",
                name = "Coco",
                display = "Coco: Hội Ngộ Diệu Kỳ",
                description = "Miguel, một cậu bé yêu âm nhạc, vô tình bước vào Xứ Sở Người Chết và phải tìm cách trở về trước khi quá muộn, đồng thời khám phá bí mật gia đình.",
                logoUrl = "https://image.tmdb.org/t/p/original/gGEsBPAijhVUFoiNpgZXqRVWJt2.jpg",
                streamUrl = "https://example.com/stream/coco.m3u8",
                shareUrl = "https://example.com/share/coco",
                imdb = "8.4",
                quality = "4K",
                rating = "4.8",
                year = "2017",
                episode = "Film"
            ),
            Channel(
                id = "animation_008",
                name = "Moana",
                display = "Moana: Hành Trình Của Moana",
                description = "Moana, con gái của tù trưởng, lên đường tìm kiếm nữ thần Te Fiti cùng Maui, một vị thần nửa người để cứu hòn đảo của cô khỏi lời nguyền đen tối.",
                logoUrl = "https://image.tmdb.org/t/p/original/4JeejGugONWpJkbnvL12hVoYEDa.jpg",
                streamUrl = "https://example.com/stream/moana.m3u8",
                shareUrl = "https://example.com/share/moana",
                imdb = "7.6",
                quality = "4K",
                rating = "4.4",
                year = "2016",
                episode = "Film"
            ),
            Channel(
                id = "animation_009",
                name = "Frozen",
                display = "Frozen: Nữ Hoàng Băng Giá",
                description = "Anna lên đường tìm chị gái Elsa, người có sức mạnh băng tuyết đã vô tình biến vương quốc thành mùa đông vĩnh cửu, để cứu vương quốc Arendelle.",
                logoUrl = "https://image.tmdb.org/t/p/original/kgwjIb2JDHRhNk13lmSxiClFjVk.jpg",
                streamUrl = "https://example.com/stream/frozen.m3u8",
                shareUrl = "https://example.com/share/frozen",
                imdb = "7.4",
                quality = "4K",
                rating = "4.3",
                year = "2013",
                episode = "Film"
            ),
            Channel(
                id = "animation_010",
                name = "Turning Red",
                display = "Turning Red: Biến Đỏ",
                description = "Mei Lee, một cô gái 13 tuổi tự tin, phát hiện ra rằng cô biến thành một con gấu trúc đỏ khổng lồ bất cứ khi nào cô quá phấn khích. Cô phải cân bằng giữa việc làm một cô gái tuổi teen và di sản gia đình.",
                logoUrl = "https://image.tmdb.org/t/p/original/qsdjk9oAKSQMWs0Vt5Pyfh6O4GZ.jpg",
                streamUrl = "https://example.com/stream/turning_red.m3u8",
                shareUrl = "https://example.com/share/turning_red",
                imdb = "7.0",
                quality = "4K",
                rating = "4.1",
                year = "2022",
                episode = "Film"
            ),
            Channel(
                id = "animation_011",
                name = "Soul",
                display = "Soul: Cuộc Sống Nhiệm Màu",
                description = "Joe Gardner, một giáo viên âm nhạc, có giấc mơ trở thành nhạc sĩ jazz. Sau một tai nạn, linh hồn anh bị tách khỏi cơ thể và anh phải tìm đường trở lại Trái Đất.",
                logoUrl = "https://image.tmdb.org/t/p/original/hm58Jw4Lw8OIeECIq5qyPYhAeRJ.jpg",
                streamUrl = "https://example.com/stream/soul.m3u8",
                shareUrl = "https://example.com/share/soul",
                imdb = "8.0",
                quality = "4K",
                rating = "4.6",
                year = "2020",
                episode = "Film"
            ),
            Channel(
                id = "animation_012",
                name = "Luca",
                display = "Luca: Mùa Hè Của Luca",
                description = "Luca, một quái vật biển trẻ, trải nghiệm một mùa hè khó quên trên bờ biển Ý. Anh kết bạn với Alberto và cùng nhau khám phá thị trấn, che giấu bí mật thật sự của mình.",
                logoUrl = "https://image.tmdb.org/t/p/original/620hnMVLu6RSZW6a5rwO8gqpt0t.jpg",
                streamUrl = "https://example.com/stream/luca.m3u8",
                shareUrl = "https://example.com/share/luca",
                imdb = "7.4",
                quality = "4K",
                rating = "4.2",
                year = "2021",
                episode = "Film"
            ),
            Channel(
                id = "animation_013",
                name = "Raya and the Last Dragon",
                display = "Raya Và Rồng Thần Cuối Cùng",
                description = "Trong thế giới giả tưởng Kumandra, chiến binh Raya phải tìm rồng cuối cùng để đoàn kết các vương quốc chia rẽ và đánh bại lực lượng tà ác đe dọa thế giới.",
                logoUrl = "https://image.tmdb.org/t/p/original/lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg",
                streamUrl = "https://example.com/stream/raya.m3u8",
                shareUrl = "https://example.com/share/raya",
                imdb = "7.3",
                quality = "4K",
                rating = "4.3",
                year = "2021",
                episode = "Film"
            ),
            Channel(
                id = "animation_014",
                name = "The Incredibles",
                display = "The Incredibles: Gia Đình Siêu Nhân",
                description = "Bob Parr đã từng là siêu anh hùng vĩ đại nhất. 15 năm sau, anh và vợ Helen sống cuộc sống bình thường với 3 con. Nhưng khi nguy hiểm xuất hiện, gia đình phải hành động.",
                logoUrl = "https://image.tmdb.org/t/p/original/2LqaLgk4Z226KkgPJuiOQ58wvrm.jpg",
                streamUrl = "https://example.com/stream/incredibles.m3u8",
                shareUrl = "https://example.com/share/incredibles",
                imdb = "8.0",
                quality = "4K",
                rating = "4.6",
                year = "2004",
                episode = "Film"
            ),
            Channel(
                id = "animation_015",
                name = "Finding Nemo",
                display = "Finding Nemo: Đi Tìm Nemo",
                description = "Marlin, một chú cá hề, lên đường tìm con trai Nemo qua đại dương. Trên đường đi, anh gặp Dory, một chú cá bị mất trí nhớ nhưng luôn lạc quan.",
                logoUrl = "https://image.tmdb.org/t/p/original/eHuGQ10FUzK1mdOY69wF5pGgEf5.jpg",
                streamUrl = "https://example.com/stream/finding_nemo.m3u8",
                shareUrl = "https://example.com/share/finding_nemo",
                imdb = "8.2",
                quality = "4K",
                rating = "4.7",
                year = "2003",
                episode = "Film"
            )
        )
    }

    private fun getHorrorMovies(): List<Channel> {
        return listOf(
            Channel(
                id = "horror_001",
                name = "The Nun II",
                display = "The Nun II: Ác Quỷ Ma Sơ 2",
                description = "Năm 1956, bốn năm sau các sự kiện tại Tu viện Thánh Carta, Cha Maurice và Sister Irene phải đối mặt với ác quỷ Valak một lần nữa.",
                logoUrl = "https://image.tmdb.org/t/p/original/5gzzkR7y3hnY8AD1wXjCnVlHba5.jpg",
                streamUrl = "https://example.com/stream/nun_2.m3u8",
                shareUrl = "https://example.com/share/nun_2",
                imdb = "7.5",
                quality = "4K",
                rating = "4.0",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "horror_002",
                name = "Insidious 5",
                display = "Insidious: The Red Door",
                description = "Josh Lambert đưa con trai Dalton đến trường đại học. Tuy nhiên, những con quỷ bị kìm nén trong quá khứ đột nhiên quay trở lại để ám ảnh cả cha và con.",
                logoUrl = "https://image.tmdb.org/t/p/original/d07phJqCx6z5wILDYqkyraorDPi.jpg",
                streamUrl = "https://example.com/stream/insidious_5.m3u8",
                shareUrl = "https://example.com/share/insidious_5",
                imdb = "7.3",
                quality = "4K",
                rating = "3.9",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "horror_003",
                name = "Talk to Me",
                display = "Talk to Me: Nói Với Tôi",
                description = "Khi một nhóm bạn phát hiện cách triệu hồi linh hồn bằng một bàn tay được ướp xác, họ trở nên nghiện cảm giác mạnh mới, cho đến khi một trong số họ đi quá xa.",
                logoUrl = "https://image.tmdb.org/t/p/original/kdPMUMJzyYAc4roD52qavX0nLIC.jpg",
                streamUrl = "https://example.com/stream/talk_to_me.m3u8",
                shareUrl = "https://example.com/share/talk_to_me",
                imdb = "8.0",
                quality = "4K",
                rating = "4.3",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "horror_004",
                name = "Evil Dead Rise",
                display = "Evil Dead Rise: Quỷ Dữ Trỗi Dậy",
                description = "Một cuộc đoàn tụ gia đình trở thành ác mộng khi chúng phát hiện ra cuốn sách của người chết và vô tình triệu hồi các con quỷ ăn thịt người.",
                logoUrl = "https://image.tmdb.org/t/p/original/mIBCtPvKZQlxubxKMeViO2UrP3q.jpg",
                streamUrl = "https://example.com/stream/evil_dead_rise.m3u8",
                shareUrl = "https://example.com/share/evil_dead_rise",
                imdb = "7.8",
                quality = "4K",
                rating = "4.2",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "horror_005",
                name = "Smile",
                display = "Smile: Nụ Cười Ma Quái",
                description = "Sau khi chứng kiến một sự cố kỳ lạ, chấn động liên quan đến một bệnh nhân, bác sĩ Rose Cotter bắt đầu trải nghiệm những sự việc đáng sợ không thể giải thích.",
                logoUrl = "https://image.tmdb.org/t/p/original/aPqcQwu4VGEewPhagWNncDbJ9Xp.jpg",
                streamUrl = "https://example.com/stream/smile.m3u8",
                shareUrl = "https://example.com/share/smile",
                imdb = "7.6",
                quality = "4K",
                rating = "4.1",
                year = "2022",
                episode = "Film"
            )
        )
    }

    private fun getComedyMovies(): List<Channel> {
        return listOf(
            Channel(
                id = "comedy_001",
                name = "The Out-Laws",
                display = "The Out-Laws: Bố Mẹ Vợ Siêu Quậy",
                description = "Một người quản lý ngân hàng tương lai bắt đầu nghi ngờ rằng cha mẹ chồng sắp cưới của anh ta có thể là những tên trộm ngân hàng khét tiếng đã bỏ trốn.",
                logoUrl = "https://image.tmdb.org/t/p/original/aRmXi7qg8GOcYdS5X7US0hBGvLw.jpg",
                streamUrl = "https://example.com/stream/out_laws.m3u8",
                shareUrl = "https://example.com/share/out_laws",
                imdb = "6.8",
                quality = "HD",
                rating = "3.5",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "comedy_002",
                name = "You People",
                display = "You People: Những Người Đó",
                description = "Một cặp đôi mới phải đối mặt với sự khác biệt văn hóa và sự xung đột giữa gia đình họ khi họ quyết định kết hôn.",
                logoUrl = "https://image.tmdb.org/t/p/original/x5E4TndwASNkaK2hwgeYfsFF8wk.jpg",
                streamUrl = "https://example.com/stream/you_people.m3u8",
                shareUrl = "https://example.com/share/you_people",
                imdb = "6.9",
                quality = "HD",
                rating = "3.6",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "comedy_003",
                name = "No Hard Feelings",
                display = "No Hard Feelings: Không Hận Thù",
                description = "Một phụ nữ được thuê bởi cha mẹ của một thanh niên nhút nhát để hẹn hò với anh ta trước khi anh ta đi học đại học.",
                logoUrl = "https://image.tmdb.org/t/p/original/4K7gQjD019xjIYK8U0H3u2VmM2E.jpg",
                streamUrl = "https://example.com/stream/no_hard_feelings.m3u8",
                shareUrl = "https://example.com/share/no_hard_feelings",
                imdb = "7.2",
                quality = "HD",
                rating = "3.8",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "comedy_004",
                name = "Vacation Friends 2",
                display = "Vacation Friends 2: Bạn Nghỉ Dưỡng 2",
                description = "Cặp đôi mới cưới Marcus và Emily mời người bạn không thể đoán trước của họ Ron và Kyla đến kỳ nghỉ ở vùng Caribê.",
                logoUrl = "https://image.tmdb.org/t/p/original/kPFlvGihf2JtRWRCt0Fh1mHAVPP.jpg",
                streamUrl = "https://example.com/stream/vacation_friends_2.m3u8",
                shareUrl = "https://example.com/share/vacation_friends_2",
                imdb = "7.0",
                quality = "HD",
                rating = "3.7",
                year = "2023",
                episode = "Film"
            ),
            Channel(
                id = "comedy_005",
                name = "Strays",
                display = "Strays: Chó Hoang",
                description = "Một chó bị bỏ rơi kết bạn với những con chó hoang khác và cùng nhau lên kế hoạch trả thù chủ cũ độc ác của mình.",
                logoUrl = "https://image.tmdb.org/t/p/original/muDaKftykz9Nj1mhRheMdbuNI9Z.jpg",
                streamUrl = "https://example.com/stream/strays.m3u8",
                shareUrl = "https://example.com/share/strays",
                imdb = "7.3",
                quality = "HD",
                rating = "3.9",
                year = "2023",
                episode = "Film"
            )
        )
    }
}