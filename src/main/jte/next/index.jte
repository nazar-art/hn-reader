@import letscode.hnreader.api.dto.HnItem

@param java.util.List<HnItem> stories
@param int currentPage
@param int totalPages
@param int pageSize

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hacker News Top 100</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css">
</head>
<body>
<main class="container">
    <h1>Top 100 Hacker News Stories</h1>

    <b>Jte template</b>

    @if (totalPages > 1)
        @template.next.fragments.pagination(currentPage = currentPage, totalPages = totalPages)
    @endif

    @for (var story : stories)
        @template.next.fragments.news-item(story = story)
    @else
        No news :-(
    @endfor

    @if (totalPages > 1)
        @template.next.fragments.pagination(currentPage = currentPage, totalPages = totalPages)
    @endif
</main>
</body>
</html>
