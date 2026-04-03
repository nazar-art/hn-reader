@import letscode.hnreader.api.dto.HnItem

@param HnItem story

<article class="news-item">
    <header>
        <a href="${story.getUrl()}" target="_blank">
            ${story.getTitle()}
        </a>
    </header>

    $unsafe{story.getText()}

    <footer>
        <span>${story.getScore()}</span> points by
        <span>${story.getBy()}</span> |
        <a href="https://news.ycombinator.com/item?id=${story.getId()}" target="_blank">comments</a>
    </footer>
</article>
