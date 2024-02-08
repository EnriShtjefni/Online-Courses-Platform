document.addEventListener("DOMContentLoaded", function () {
    const slider = document.querySelector(".slider");
    const slides = slider.querySelectorAll("img");
    const totalSlides = slides.length;
    const slideWidth = slides[0].clientWidth;
    let count = 1;


    const firstClone = slides[0].cloneNode(true);
    const lastClone = slides[totalSlides - 1].cloneNode(true);

    slider.appendChild(firstClone);
    slider.insertBefore(lastClone, slides[0]);


    slider.style.transform = "translateX(" + (-slideWidth * count) + "px)";

    function nextSlide() {
        count++;
        slider.style.transition = "transform 0.5s ease-in-out";
        slider.style.transform = "translateX(" + (-slideWidth * count) + "px)";
    }

    function prevSlide() {
        count--;
        slider.style.transition = "transform 0.5s ease-in-out";
        slider.style.transform = "translateX(" + (-slideWidth * count) + "px)";
    }

    function transitionEnd() {
        if (count >= totalSlides + 1) {
            count = 1;
            slider.style.transition = "none";
            slider.style.transform = "translateX(" + (-slideWidth * count) + "px)";
        }
        if (count <= 0) {
            count = totalSlides;
            slider.style.transition = "none";
            slider.style.transform = "translateX(" + (-slideWidth * count) + "px)";
        }
    }

    slider.addEventListener("transitionend", function () {
        transitionEnd();
        setTimeout(() => {
            slider.style.transition = "transform 0.5s ease-in-out";
        }, 0);
    });

    setInterval(nextSlide, 1800);
});
