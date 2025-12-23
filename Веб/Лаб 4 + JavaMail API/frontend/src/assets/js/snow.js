const storageKey = 'snow';

function getRndInteger(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function getRndFloat(min, max) {
  return (Math.random() * (max - min) + min).toFixed(1);
}

function initSnow() {
  const snow = document.querySelector('.snow');
  const snowToggle = document.querySelector('.snow-toggle');
  const snowflakes = document.querySelectorAll('.snow__flake');

  if (!snow || !snowToggle || snowflakes.length === 0) {
    requestAnimationFrame(initSnow);
    return;
  }

  snowflakes.forEach(snowflake => {
    snowflake.style.fontSize = getRndFloat(0.7, 1.5) + 'em';
    snowflake.style.animationDuration = getRndInteger(20, 30) + 's';
    snowflake.style.animationDelay =
      getRndInteger(-1, snowflakes.length / 2) + 's';
  });

  function changeSnowAnimation(animationName) {
    snow.style.setProperty('--animation-name', animationName || 'none');
  }

  snowToggle.addEventListener('change', event => {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) return;

    changeSnowAnimation(target.value);
    localStorage.setItem(storageKey, target.value);
  });

  let currentStorage = localStorage.getItem(storageKey);

  if (currentStorage) {
    const input = snowToggle.querySelector(
      `.snow-toggle__control[value='${currentStorage}']`
    );
    if (input instanceof HTMLInputElement) {
      input.checked = true;
    }
  }

  changeSnowAnimation(currentStorage);

  window.addEventListener('storage', () => {
    changeSnowAnimation(localStorage.getItem(storageKey));
  });
}

document.addEventListener('DOMContentLoaded', () => {
  initSnow();
});
